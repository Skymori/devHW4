package ua.goit.jdbc.service.services;

import ua.goit.jdbc.dao.SkillDAO;
import ua.goit.jdbc.repositories.one_entity_repositories.Repository;
import ua.goit.jdbc.dto.skillDTO;
import ua.goit.jdbc.converters.SkillConverter;

import java.util.List;

public class SkillService {
    private Repository<SkillDAO> repository;

    public SkillService(Repository<SkillDAO> repository) {
        this.repository = repository;
    }

    public skillDTO create(skillDTO skillDTO) {
        SkillDAO skillDAO = SkillConverter.toSkillDAO(skillDTO);
        SkillDAO createdSkillDAO = repository.create(skillDAO);
        return SkillConverter.fromSkillDAO(repository.findById(createdSkillDAO.getSkillId()));
    }

    public skillDTO findById(int companyId) {
        return SkillConverter.fromSkillDAO(repository.findById(companyId));
    }

    public skillDTO update(skillDTO skillDTO) {
        SkillDAO updatedSkillDAO = repository.update(SkillConverter.toSkillDAO(skillDTO));
        return SkillConverter.fromSkillDAO(updatedSkillDAO);
    }

    public skillDTO deleteById(int companyId) {
        return SkillConverter.fromSkillDAO(repository.deleteById(companyId));
    }

    public skillDTO deleteByObject(skillDTO skillDTO) {
        SkillDAO deletedSkillDAO = repository.deleteByObject(SkillConverter.toSkillDAO(skillDTO));
        return SkillConverter.fromSkillDAO(deletedSkillDAO);
    }

    public List<skillDTO> findAll() {
        return SkillConverter.allFromSkillDAO(repository.findAll());
    }
}
