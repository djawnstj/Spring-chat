package com.djawnstj.stomp.member.entity

import com.djawnstj.stomp.common.entity.BaseEntity
import jakarta.persistence.*
import org.djawnstj.store.member.entity.MemberRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(
    name = "member",
    uniqueConstraints = [UniqueConstraint(name = "uk_member_login_id", columnNames = ["login_id"])]
)
class Member(
    loginId: String,
    loginPassword: String,
    name: String,
    role: MemberRole = MemberRole.STAFF
): BaseEntity(), UserDetails {

    @field:Column(name = "login_id", nullable = false, unique = true)
    var loginId: String = loginId
        protected set
    @field:Column(name = "login_password", nullable = false)
    var loginPassword: String = loginPassword
        protected set
    @field:Column(name = "name", nullable = false)
    var name: String = name
        protected set
    @field:Column(name = "role", nullable = false)
    @field:Enumerated(EnumType.STRING)
    var role: MemberRole = role
        protected set

    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(this.role.name))

    override fun getPassword(): String = this.loginPassword

    override fun getUsername(): String? = this.loginId

    override fun isAccountNonExpired(): Boolean = this.deletedAt == null

    override fun isAccountNonLocked(): Boolean = this.deletedAt == null

    override fun isCredentialsNonExpired(): Boolean = this.deletedAt == null

    override fun isEnabled(): Boolean = this.deletedAt == null

}
