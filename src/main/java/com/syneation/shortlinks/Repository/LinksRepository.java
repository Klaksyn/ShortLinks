package com.syneation.shortlinks.Repository;

import com.syneation.shortlinks.entity.Links;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LinksRepository extends JpaRepository<Links, Integer> {

    Optional<Links> findById(Long id);

    List<Links> findLinksByCreator_Id(Long creatorId);

}
