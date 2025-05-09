package org.example.api256green.Repositores;

import org.example.api256green.entites.TableKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface TableKeyRepository extends JpaRepository<TableKey,Integer> {
    @Query(value = """
select 
 id, keyword, createdate,status
 from table_key
""",nativeQuery = true)
    List<Object[]> getAllKey();
}
