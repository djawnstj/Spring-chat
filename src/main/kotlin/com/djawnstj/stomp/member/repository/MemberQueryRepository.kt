package com.djawnstj.stomp.member.repository

import com.djawnstj.stomp.member.entity.Member
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class MemberQueryRepository(
    private val memberJpaRepository: MemberJpaRepository
) {

    fun findByPhoneNumber(loginId: String): Member? = memberJpaRepository.findAll {
        select(
            entity(Member::class)
        ).from(
            entity(Member::class)
        ).where(
            path(Member::deletedAt).isNull()
                .and(
                    path(Member::loginId).equal(loginId)
                )
        )
    }.firstOrNull()

}
