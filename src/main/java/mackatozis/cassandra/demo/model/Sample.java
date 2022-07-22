package mackatozis.cassandra.demo.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("SAMPLE")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Sample implements Serializable {

    @Serial
    private static final long serialVersionUID = 3608257287658031254L;

    @PrimaryKey("EXTERNAL_ID")
    private String externalId;

    @Column("NAME")
    private String name;

    @Column("LAST_UPDATE")
    private Instant updateTimestamp;

}
