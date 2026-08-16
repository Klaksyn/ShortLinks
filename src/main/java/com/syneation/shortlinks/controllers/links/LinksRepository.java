package com.syneation.shortlinks.controllers.links;

import com.syneation.shortlinks.controllers.links.model.Links;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface LinksRepository extends JpaRepository<Links, Long> {

    List<Links> findLinksByCreator_Id(Long creatorId);

    @Modifying
    @Transactional
    @Query("update Links l set l.isActive = :isActive where l.id = :id")
    void updateActivateStatus(@Param("id") Long id, @Param("isActive") boolean isActive);

}
