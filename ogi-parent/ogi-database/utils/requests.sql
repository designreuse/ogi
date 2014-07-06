
-- Select all property to synchronise to seloger.com
select * from TA_PROPERTY realproper0_ 
inner join TA_PARTNER_REQUEST partnersre1_ on realproper0_.PRO_ID=partnersre1_.PRO_ID 
where partnersre1_.REQ_PARTNER='seloger' 
and (partnersre1_.REQ_TYPE in ('push' , 'push_ack')) 
and  not (exists (
	select partnerreq2_.REQ_ID 
		from TA_PARTNER_REQUEST partnerreq2_ 
		where partnerreq2_.PRO_ID=partnersre1_.PRO_ID 
		and partnerreq2_.REQ_PARTNER=partnersre1_.REQ_PARTNER 
		and partnerreq2_.REQ_MODIFICATION_DATE>partnersre1_.REQ_MODIFICATION_DATE)
	);
	
-- Select all partner action for a property
SELECT r.* FROM TA_PARTNER_REQUEST r
JOIN TA_PROPERTY p ON r.PRO_ID=p.PRO_ID
WHERE p.PRO_REFERENCE='M8'
ORDER BY REQ_MODIFICATION_DATE ASC;
	


-- Replace / to \ (import linux --> windows)
UPDATE TA_DOCUMENT SET
DOC_PATH=REPLACE(DOC_PATH, '/', '\\');