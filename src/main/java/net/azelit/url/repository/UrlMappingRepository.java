package net.azelit.url.repository;

import net.azelit.url.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    UrlMapping findByHash(String hash);
    UrlMapping findByOriginalUrl(String originalUrl);
    boolean existsByHash(String hash);
}
