package com.learnbatch.segmentcodebatch;

import com.learnbatch.segmentcodebatch.model.SegmentCode;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfiguration {
  private final DateFormat localDateformatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

  @Autowired public JobBuilderFactory jobBuilderFactory;

  @Autowired public StepBuilderFactory stepBuilderFactory;

  @Autowired public DataConverter dataConverter;

  @Autowired private JobLauncher jobLauncher;
  @Autowired private Job job;

  @Bean
  public DataSource dataSource() {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://localhost:3306/cc_citizen");
    dataSource.setUsername("root");
    dataSource.setPassword("1234");

    return dataSource;
  }

  @Bean
  public StaxEventItemReader<SegmentCode> reader() {
    StaxEventItemReader<SegmentCode> reader = new StaxEventItemReader<>();
    reader.setResource(new ClassPathResource("segment.xml"));
    reader.setFragmentRootElementName("SegmentCode");

    Map<String, String> aliases = new HashMap<>();
    aliases.put("SegmentCode", "com.learnbatch.segmentcodebatch.model.SegmentCode");

    XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
    xStreamMarshaller.setAliases(aliases);
    xStreamMarshaller.setConverters(dataConverter);
    reader.setUnmarshaller(xStreamMarshaller);

    return reader;
  }

  @Scheduled(cron = "0 */1 * * * ?")
  public void perform() throws Exception {
    JobParameters params =
        new JobParametersBuilder()
            .addString("JobID", localDateformatter.format(new Date(System.currentTimeMillis())))
            .toJobParameters();

    jobLauncher.run(job, params);
  }

  @Bean
  public JdbcBatchItemWriter<SegmentCode> writer() {
    JdbcBatchItemWriter<SegmentCode> writer = new JdbcBatchItemWriter<>();

    writer.setDataSource(dataSource());
    writer.setSql(
        "insert into lookup_segment_code(sk,code,description,rank,memo,color,font_color,default_duration,updated_by_sk,updated_on) values(?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
    writer.setItemPreparedStatementSetter(new UserItemPreparedStmSetter());

    return writer;
  }

  @Bean
  public Step step1() {
    return stepBuilderFactory
        .get("step1")
        .<SegmentCode, SegmentCode>chunk(10)
        .reader(reader())
        .writer(writer())
        .build();
  }

  @Bean
  public Job job() {
    return jobBuilderFactory.get("job").flow(step1()).end().build();
  }

  private class UserItemPreparedStmSetter implements ItemPreparedStatementSetter<SegmentCode> {

    @Override
    public void setValues(SegmentCode segment, PreparedStatement ps) throws SQLException {
      ps.setString(1, segment.getSk());
      ps.setString(2, segment.getCode());
      ps.setString(3, segment.getDescription());
      ps.setInt(4, segment.getRank());
      ps.setBoolean(5, segment.isRequiresMemo());
      ps.setInt(6, segment.getColor());
      ps.setInt(7, segment.getFontColor());
      ps.setInt(8, segment.getDefaultDuration());
      ps.setString(9, segment.getUpdatedBy().getSk());
      ps.setString(10, String.valueOf(segment.getUpdatedOn()));
    }
  }
}
