package com.campusos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DemoData {
  @Bean CommandLineRunner seed(TicketRepository tickets,ResourceRepository resources,KnowledgeRepository knowledge,OpportunityRepository opportunities) {
    return args -> {
      if(resources.count()==0) {
        resources.saveAll(java.util.List.of(new CampusResource("Library Study Room 01","STUDY_ROOM","Central Library",false),
          new CampusResource("Computer Lab A","LAB","Engineering Block",true),
          new CampusResource("Seminar Hall","HALL","Admin Block",true),
          new CampusResource("Basketball Court","SPORTS","Sports Complex",false)));
      }
      if(tickets.count()==0) {
        Ticket a=new Ticket(); a.title="Wi-Fi not working"; a.description="Several students cannot connect."; a.category="NETWORK"; a.location="Central Library"; a.priority="HIGH"; a.reporter="student@campusos.demo"; a.assignedDepartment="IT Services"; tickets.save(a);
        Ticket b=new Ticket(); b.title="Library Wi-Fi unavailable"; b.description="Connection fails near reading area."; b.category="NETWORK"; b.location="Central Library"; b.priority="MEDIUM"; b.reporter="faculty@campusos.demo"; b.assignedDepartment="IT Services"; tickets.save(b);
        Ticket c=new Ticket(); c.title="Projector not turning on"; c.description="Projector in room E-204 needs inspection."; c.category="EQUIPMENT"; c.location="Engineering Block"; c.priority="MEDIUM"; c.reporter="faculty@campusos.demo"; c.assignedDepartment="Campus Services"; tickets.save(c);
      }
      if(knowledge.count()==0) knowledge.saveAll(java.util.List.of(
        new KnowledgeArticle("Examination procedures","Students should check the official examination timetable and contact the Examination Office for approved accommodations or timetable corrections.","Examination Office"),
        new KnowledgeArticle("Library policies","Keep library access cards available, return borrowed items by the due date, and contact library staff for renewals.","Library Services"),
        new KnowledgeArticle("Hostel maintenance requests","Report water, electricity, or room maintenance issues through the campus helpdesk and include the hostel block and room area.","Hostel Office")));
      if(opportunities.count()==0) opportunities.saveAll(java.util.List.of(
        new Opportunity("Campus Innovation Hackathon","HACKATHON","Build a prototype for a campus challenge with a student team.","2026-11-20","https://example.com/hackathon",true),
        new Opportunity("Frontend Development Workshop","WORKSHOP","A hands-on introduction to accessible React interfaces.","2026-11-08","https://example.com/workshop",true),
        new Opportunity("Student Research Internship","INTERNSHIP","Explore a short research placement; verify eligibility with the organizer.","2026-12-01","https://example.com/internship",false)));
    };
  }
}
