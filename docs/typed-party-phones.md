# Typed party telephone addresses

`IAddressService` provides stateless save, find and end operations for owned
`PartyPhoneDTO` entries. The type is an existing AddressTelephoneClassifications
telephone, cell, fax or pager classification scoped to the Address concept.
Numbers and optional extensions are protected involved-party identification rows
linked to the telephone address. The address anchor stores no plaintext number.
Save requires authenticated encryption, party and existing-row write permissions;
read checks party, relationship, address and protected-row read permissions.
Foreign address IDs fail closed. Retiring a phone archives its active relationships
without deleting the address or touching physical/postal address entries.

Profile Master exposes a repeatable phones collection. Sparse omission preserves
records; an explicit collection saves entries, retires removed phone relationships,
and clears flattened phone attributes only after successful persistence. Existing
local data is not migrated implicitly. The service uses the caller's stateless
transaction throughout and waits for all writes before returning.
