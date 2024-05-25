package com.djawnstj.stomp.member.repository

import com.djawnstj.stomp.member.entity.Member
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberJpaRepository: JpaRepository<Member, Long>, KotlinJdslJpqlExecutor {
}
