package mackatozis.cassandra.demo.repository;

import mackatozis.cassandra.demo.model.Sample;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends CassandraRepository<Sample, String> {

}
