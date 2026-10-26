package com.campusos;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Validated
public class ApiController {
  private final TicketRepository tickets;
  private final ResourceRepository resources;
  private final BookingRepository bookings;
  private final KnowledgeRepository knowledge;
  private final OpportunityRepository opportunities;

  public ApiController(TicketRepository tickets, ResourceRepository resources, BookingRepository bookings,
                       KnowledgeRepository knowledge, OpportunityRepository opportunities) {
    this.tickets=tickets; this.resources=resources; this.bookings=bookings; this.knowledge=knowledge; this.opportunities=opportunities;
  }

  @GetMapping("/dashboard")
  public Map<String,Object> dashboard(@RequestParam(defaultValue="STUDENT") String role) {
    Map<String,Object> m=new LinkedHashMap<>();
    m.put("role",role); m.put("openTickets",tickets.findByStatus("OPEN").size());
    m.put("totalTickets",tickets.count()); m.put("resources",resources.count());
    m.put("opportunities",opportunities.count()); m.put("actions",switch(role.toUpperCase()) {
      case "CAMPUS_ADMIN" -> List.of("Review campus analytics","Manage campus users","Verify incidents");
      case "SERVICE_STAFF" -> List.of("Review assigned tickets","Update incident status","Resolve overdue issues");
      case "FACULTY" -> List.of("Review announcements","Find a resource","Browse opportunities");
      default -> List.of("Report a campus issue","Book a study room","Search campus policies");
    });
    return m;
  }

  @GetMapping("/tickets") public List<Ticket> listTickets() { return tickets.findAll(); }
  @PostMapping("/tickets") @ResponseStatus(HttpStatus.CREATED)
  public Ticket createTicket(@Valid @RequestBody Ticket ticket) {
    ticket.id=null; ticket.status="OPEN"; ticket.createdAt=LocalDateTime.now(); ticket.updatedAt=ticket.createdAt;
    if(ticket.category==null || ticket.category.isBlank()) ticket.category="GENERAL";
    if(ticket.location==null || ticket.location.isBlank()) ticket.location="Unspecified";
    if(ticket.priority==null) ticket.priority="MEDIUM";
    ticket.assignedDepartment=assign(ticket.category);
    return tickets.save(ticket);
  }
  @PatchMapping("/tickets/{id}/status")
  public Ticket updateTicket(@PathVariable Long id,@RequestParam String status) {
    Ticket t=tickets.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Ticket not found"));
    if(!Set.of("OPEN","IN_PROGRESS","RESOLVED","CLOSED").contains(status.toUpperCase()))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid status");
    t.status=status.toUpperCase(); t.updatedAt=LocalDateTime.now(); return tickets.save(t);
  }
  private String assign(String category) {
    return switch(category.toUpperCase()) {
      case "NETWORK","WIFI" -> "IT Services";
      case "HOSTEL","WATER","ELECTRICITY" -> "Facilities";
      case "LIBRARY" -> "Library Services";
      default -> "Campus Services";
    };
  }

  @GetMapping("/incidents")
  public List<Map<String,Object>> incidents() {
    Map<String,List<Ticket>> groups=tickets.findAll().stream().filter(t->!"RESOLVED".equals(t.status)&&!"CLOSED".equals(t.status))
      .collect(Collectors.groupingBy(t->String.valueOf(t.category).toLowerCase()+"|"+String.valueOf(t.location).trim().toLowerCase()));
    List<Map<String,Object>> result=new ArrayList<>();
    groups.values().stream().filter(g->g.size()>=2).forEach(g->{
      Ticket first=g.get(0); Map<String,Object> x=new LinkedHashMap<>();
      x.put("category",first.category); x.put("location",first.location); x.put("reportCount",g.size());
      x.put("severity",g.size()>=5?"HIGH":g.size()>=3?"MEDIUM":"LOW");
      x.put("status","POTENTIAL_UNVERIFIED"); x.put("firstReported",g.stream().map(t->t.createdAt).min(LocalDateTime::compareTo).orElse(LocalDateTime.now()));
      x.put("ticketIds",g.stream().map(t->t.id).toList()); result.add(x);
    });
    return result;
  }

  @GetMapping("/resources") public List<CampusResource> listResources() { return resources.findAll(); }
  @GetMapping("/bookings") public List<ResourceBooking> listBookings() { return bookings.findAll(); }
  @PostMapping("/bookings") @ResponseStatus(HttpStatus.CREATED)
  public ResourceBooking createBooking(@RequestBody ResourceBooking b) {
    if(b.resourceId==null || !resources.existsById(b.resourceId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unknown resource");
    if(b.startAt==null || b.endAt==null || !b.startAt.isBefore(b.endAt)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Start must be before end");
    boolean conflict=bookings.findByResourceIdAndStatus(b.resourceId,"CONFIRMED").stream()
      .anyMatch(old->b.startAt.isBefore(old.endAt)&&b.endAt.isAfter(old.startAt));
    if(conflict) throw new ResponseStatusException(HttpStatus.CONFLICT,"This resource is already booked for that time");
    b.id=null; b.status="CONFIRMED"; return bookings.save(b);
  }

  @GetMapping("/knowledge")
  public List<Map<String,Object>> searchKnowledge(@RequestParam(defaultValue="") String q) {
    String query=q.toLowerCase().trim();
    return knowledge.findAll().stream().filter(a->query.isBlank() || (a.title+" "+a.content+" "+a.department).toLowerCase().contains(query))
      .map(a->{Map<String,Object> m=new LinkedHashMap<>();m.put("id",a.id);m.put("title",a.title);m.put("content",a.content);m.put("department",a.department);m.put("source",a.title);return m;}).toList();
  }
  @GetMapping("/opportunities") public List<Opportunity> listOpportunities() { return opportunities.findAll(); }
  @PostMapping("/opportunities") @ResponseStatus(HttpStatus.CREATED)
  public Opportunity createOpportunity(@RequestBody Opportunity o) { o.id=null; return opportunities.save(o); }
  @PostMapping("/opportunities/{id}/bookmark")
  public Map<String,Object> bookmark(@PathVariable Long id,@RequestParam(defaultValue="student@campusos.demo") String user) {
    if(!opportunities.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Opportunity not found");
    return Map.of("opportunityId",id,"user",user,"bookmarked",true,"message","Bookmark accepted for demo");
  }
  @GetMapping("/notifications")
  public List<Map<String,String>> notifications() {
    return List.of(Map.of("title","Welcome to CampusOS","message","Your campus workspace is ready."),
      Map.of("title","Campus Pulse","message","Potential incidents are shown as unverified until staff confirm them."));
  }
  @GetMapping("/analytics")
  public Map<String,Object> analytics() {
    List<Ticket> all=tickets.findAll();
    Map<String,Long> byCategory=all.stream().collect(Collectors.groupingBy(t->String.valueOf(t.category),Collectors.counting()));
    Map<String,Long> byStatus=all.stream().collect(Collectors.groupingBy(t->String.valueOf(t.status),Collectors.counting()));
    return Map.of("ticketCount",all.size(),"openCount",all.stream().filter(t->"OPEN".equals(t.status)).count(),
      "resolvedCount",all.stream().filter(t->"RESOLVED".equals(t.status)).count(),"byCategory",byCategory,"byStatus",byStatus,
      "resourceCount",resources.count(),"opportunityCount",opportunities.count());
  }
}
