package ua.goit.jdbc.converters;


import ua.goit.jdbc.dao.SkillDAO;
import ua.goit.jdbc.dto.skillDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SkillConverter {
    public static SkillDAO toSkillDAO(skillDTO skillDTO) {
        SkillDAO skillDAO = new SkillDAO();
        skillDAO.setSkillId(skillDTO.getSkillId());
        skillDAO.setLanguage(skillDTO.getLanguage());
        skillDAO.setLevel(skillDTO.getLevel());
        return skillDAO;
    }

    public static skillDTO fromSkillDAO(SkillDAO skillDAO) {
        skillDTO skillDTO = new skillDTO();
        skillDTO.setSkillId(skillDAO.getSkillId());
        skillDTO.setLanguage(skillDAO.getLanguage());
        skillDTO.setLevel(skillDAO.getLevel());
        return skillDTO;
    }

    public static SkillDAO toSkillDAO(ResultSet resultSet) throws SQLException {
        SkillDAO skillDAO = new SkillDAO();
        skillDAO.setSkillId(resultSet.getInt("skill_id"));
        skillDAO.setLanguage(resultSet.getString("language"));
        skillDAO.setLevel(resultSet.getString("level"));
        return skillDAO;
    }

    public static List<skillDTO> allFromSkillDAO(List<SkillDAO> skillDAOS) {
        return skillDAOS.stream()
                .map(SkillConverter::fromSkillDAO)
                .collect(Collectors.toList());
    }
}
