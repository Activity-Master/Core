package com.guicedee.activitymaster.fsdm.encryption;

import io.smallrye.mutiny.Uni;

/** Trusted provider; implementations must not log key material or block the caller. */
public interface EnterpriseKeyWrapper
{
    String currentKeyReference();
    Uni<byte[]> wrap(byte[] dataKey);
    Uni<byte[]> unwrap(String keyReference, byte[] wrappedKey);
}
