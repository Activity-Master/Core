package com.guicedee.activitymaster.fsdm.encryption;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.security.keyvault.keys.cryptography.CryptographyAsyncClient;
import com.azure.security.keyvault.keys.cryptography.CryptographyClientBuilder;
import com.azure.security.keyvault.keys.cryptography.models.KeyWrapAlgorithm;
import io.smallrye.mutiny.Uni;
import java.net.URI;
import java.util.*;

/** Azure public-cloud provider. No implicit developer credentials or arbitrary stored endpoints. */
public final class AzureKeyVaultKeyWrapper implements EnterpriseKeyWrapper
{
    private final String current;
    private final Map<String, CryptographyAsyncClient> clients;

    public AzureKeyVaultKeyWrapper(String current, Set<String> permittedReferences, String managedIdentityClientId)
    {
        Set<String> allowed = new HashSet<>(permittedReferences);
        allowed.add(current);
        allowed.forEach(AzureKeyVaultKeyWrapper::validateReference);
        ManagedIdentityCredentialBuilder credentials = new ManagedIdentityCredentialBuilder();
        if (managedIdentityClientId != null && !managedIdentityClientId.isBlank()) credentials.clientId(managedIdentityClientId);
        TokenCredential credential = credentials.build();
        Map<String, CryptographyAsyncClient> configured = new HashMap<>();
        for (String reference : allowed)
            configured.put(reference, new CryptographyClientBuilder().keyIdentifier(reference).credential(credential).buildAsyncClient());
        this.current = current;
        clients = Map.copyOf(configured);
    }

    private static void validateReference(String reference)
    {
        URI uri = URI.create(reference);
        if (!"https".equals(uri.getScheme()) || uri.getHost() == null
                || !uri.getHost().matches("[a-zA-Z0-9-]+\\.vault\\.azure\\.net")
                || uri.getPort() != -1 || uri.getUserInfo() != null || uri.getQuery() != null || uri.getFragment() != null
                || !uri.getPath().matches("/keys/[a-zA-Z0-9-]+/[a-fA-F0-9]{32}"))
            throw new IllegalArgumentException("A trusted versioned Azure Key Vault key URL is required");
    }

    @Override public String currentKeyReference() { return current; }

    @Override public Uni<byte[]> wrap(byte[] dataKey)
    {
        return Uni.createFrom().completionStage(() -> clients.get(current)
                .wrapKey(KeyWrapAlgorithm.RSA_OAEP_256, dataKey).map(result -> result.getEncryptedKey()).toFuture());
    }

    @Override public Uni<byte[]> unwrap(String keyReference, byte[] wrappedKey)
    {
        CryptographyAsyncClient client = clients.get(keyReference);
        if (client == null) return Uni.createFrom().failure(new IllegalStateException("Stored Key Vault key reference is not allowlisted"));
        return Uni.createFrom().completionStage(() -> client.unwrapKey(KeyWrapAlgorithm.RSA_OAEP_256, wrappedKey)
                .map(result -> result.getKey()).toFuture());
    }
}
