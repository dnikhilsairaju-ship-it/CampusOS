package com.campusos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name="tickets")
class Ticket {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @NotBlank public String title;
  @Column(length=2000) public String description;
  public String category;
  public String location;
  public String priority;
  public String status = "OPEN";
  public String reporter;
  public String assignedDepartment = "Campus Services";
  public LocalDateTime createdAt = LocalDateTime.now();
  public LocalDateTime updatedAt = LocalDateTime.now();
  public Ticket() {}
}

@Entity
@Table(name="resources")
class CampusResource {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  public String name;
  public String type;
  public String location;
  public boolean approvalRequired;
  public CampusResource() {}
  public CampusResource(String name,String type,String location,boolean approvalRequired) {
    this.name=name; this.type=type; this.location=location; this.approvalRequired=approvalRequired;
  }
}

@Entity
@Table(name="resource_bookings")
class ResourceBooking {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  public Long resourceId;
  public String bookedBy;
  public LocalDateTime startAt;
  public LocalDateTime endAt;
  public String status = "CONFIRMED";
  public ResourceBooking() {}
}

@Entity
@Table(name="knowledge_articles")
class KnowledgeArticle {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  public String title;
  @Column(length=5000) public String content;
  public String department;
  public KnowledgeArticle() {}
  public KnowledgeArticle(String title,String content,String department) {
    this.title=title; this.content=content; this.department=department;
  }
}

@Entity
@Table(name="opportunities")
class Opportunity {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  public String title;
  public String category;
  public String description;
  public String deadline;
  public String url;
  public boolean verified;
  public Opportunity() {}
  public Opportunity(String title,String category,String description,String deadline,String url,boolean verified) {
    this.title=title; this.category=category; this.description=description; this.deadline=deadline; this.url=url; this.verified=verified;
  }
}
