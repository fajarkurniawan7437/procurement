package com.enigma.procurement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "m_vendor")
public class Vendor {
    @Id
    @GenericGenerator(strategy = "uuid2", name = "system-uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "mobile_phone", unique = true)
    private String mobilePhone;

    @Column(name = "email", unique = true)
    private String email;

    @OneToOne(targetEntity = Address.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "update_by")
    @LastModifiedBy
    private String updatedBy;

    @OneToOne
    @JoinColumn(name = "user_credential_id")
    private UserCrendential userCredential;
}
