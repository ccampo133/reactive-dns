package me.ccampo.reactivedns.model

data class DnsUpdateRequest(val domain: String, val ip: String)

data class DnsUpdateResponse(val id: String, val status: String, val submittedAt: String)
