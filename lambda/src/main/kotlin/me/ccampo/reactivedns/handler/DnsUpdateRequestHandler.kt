package me.ccampo.reactivedns.handler

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.route53.AmazonRoute53
import com.amazonaws.services.route53.model.*
import com.google.inject.Inject
import me.ccampo.reactivedns.model.DnsUpdateRequest
import me.ccampo.reactivedns.model.DnsUpdateResponse
import org.slf4j.LoggerFactory

class DnsUpdateRequestHandler @Inject constructor(
        val dnsClient: AmazonRoute53,
        val zoneId: String,
        val zoneName: String) : RequestHandler<DnsUpdateRequest, DnsUpdateResponse> {

    companion object {
        val log = LoggerFactory.getLogger(DnsUpdateRequestHandler::class.java)
    }

    override fun handleRequest(input: DnsUpdateRequest?, context: Context?): DnsUpdateResponse {
        val request = input!!
        log.info("Input: {}", request.domain)
        val result = updateRecord(dnsClient, zoneId, request.domain, request.ip)
        log.info("Result: {}", result)
        result.toString()
        return DnsUpdateResponse(result.changeInfo.id,
                result.changeInfo.status,
                result.changeInfo.submittedAt.toString())
    }

    fun updateRecord(dnsClient: AmazonRoute53, zoneId: String, domain: String, ip: String):
            ChangeResourceRecordSetsResult {
        val recordSet = ResourceRecordSet()
                .withType(RRType.A)
                .withName("$domain$zoneName")
                .withTTL(90L)
                .withResourceRecords(ResourceRecord(ip))
        val batch = ChangeBatch(listOf(Change(ChangeAction.UPSERT, recordSet)))
        val request = ChangeResourceRecordSetsRequest()
                .withHostedZoneId(zoneId)
                .withChangeBatch(batch);
        return dnsClient.changeResourceRecordSets(request)!!
    }
}
