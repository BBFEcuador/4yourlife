package com.foryourlife.admin.contifico.config.infrastructure.persistence;

import com.foryourlife.admin.contifico.config.domain.ConfigContifico;
import com.foryourlife.admin.contifico.config.domain.ConfigContificoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JPAConfigContificoRepository implements ConfigContificoRepository {
    private final JPAImplConfigContificoRepository jpaImplConfigContificoRepository;

    public JPAConfigContificoRepository(JPAImplConfigContificoRepository jpaImplConfigContificoRepository) {
        this.jpaImplConfigContificoRepository = jpaImplConfigContificoRepository;
    }

    @Override
    public ConfigContifico save(ConfigContifico configContifico) {
        return jpaImplConfigContificoRepository.save(configContifico);
    }

    @Override
    public Optional<ConfigContifico> findByCampusId(String id) {
        return jpaImplConfigContificoRepository.findByCampus_Id(id);
    }

    @Override
    public Optional<ConfigContifico> findById(String id) {
        return jpaImplConfigContificoRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        jpaImplConfigContificoRepository.deleteById(id);
    }

    @Override
    public List<ConfigContifico> findAll() {
        return jpaImplConfigContificoRepository.findAll();
    }
}
