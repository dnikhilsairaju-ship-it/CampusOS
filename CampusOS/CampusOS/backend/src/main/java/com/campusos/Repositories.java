package com.campusos;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

interface TicketRepository extends JpaRepository<Ticket,Long> {
  List<Ticket> findByStatus(String status);
  List<Ticket> findByCategoryIgnoreCaseAndLocationIgnoreCase(String category,String location);
}
interface ResourceRepository extends JpaRepository<CampusResource,Long> {}
interface BookingRepository extends JpaRepository<ResourceBooking,Long> {
  List<ResourceBooking> findByResourceIdAndStatus(Long resourceId,String status);
}
interface KnowledgeRepository extends JpaRepository<KnowledgeArticle,Long> {}
interface OpportunityRepository extends JpaRepository<Opportunity,Long> {}
