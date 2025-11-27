package com.example.SpringAi_Demo.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "refresh_tokens_jti_idx", columnList = "jti", unique = true),
        @Index(name = "refresh_tokens_login_id_idx", columnList = "login_id")
})
public class RefreshToken_Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="jti", unique = true, nullable = false, updatable = false)
    private String jti;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "login_id", nullable = false, updatable = false)
    private Login login;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean revoked;

    private String replacedByToken;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJti() {
		return jti;
	}
	public void setJti(String jti) {
		this.jti = jti;
	}
	public Login getLogin() {
		return login;
	}
	public void setLogin(Login login) {
		this.login = login;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}
	public boolean isRevoked() {
		return revoked;
	}
	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}
	public String getReplacedByToken() {
		return replacedByToken;
	}
	public void setReplacedByToken(String replacedByToken) {
		this.replacedByToken = replacedByToken;
	}
	public RefreshToken_Entity(String jti, Login login, LocalDateTime createdAt, LocalDateTime expiresAt, boolean revoked,
			String replacedByToken) {
		super();
		this.jti = jti;
		this.login = login;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
		this.revoked = revoked;
		this.replacedByToken = replacedByToken;
	}
	public RefreshToken_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
}
