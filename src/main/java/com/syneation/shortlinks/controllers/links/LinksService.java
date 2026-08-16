package com.syneation.shortlinks.controllers.links;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinksService {

    @Autowired
    private LinksRepository linksRepo;

    public void delete(Long id) {
        linksRepo.deleteById(id);
    }

    public void deactivate(Long id) {
        linksRepo.updateActivateStatus(id, false);
    }

    public void activate(Long id) {
        linksRepo.updateActivateStatus(id, true);
    }

}
