package jean.partidos;

// $example on:schema_merging$
// $example off:schema_merging$
import java.util.Properties;

import org.apache.spark.sql.AnalysisException;
// $example on:schema_merging$
// $example on:json_dataset$
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
// $example off:json_dataset$
// $example off:schema_merging$
// $example off:basic_parquet_example$
import org.apache.spark.sql.SparkSession;

public class JavaSQLDataSourceExample {

  public static void main(String[] args) {
    SparkSession spark = SparkSession
      .builder()
      .appName("My Spark Application")
      .master("local[*]")
      .getOrCreate();

    runJdbcDatasetExample(spark);

    spark.stop();
  }

  private static void runJdbcDatasetExample(SparkSession spark) {

    Properties connectionProperties = new Properties();
    connectionProperties.put("user", "root");
    connectionProperties.put("password", "root");
    Dataset<Row> jdbcDF2 = spark.read()
      .jdbc("jdbc:mysql://localhost:3306/aula", "(SELECT EXTRACTVALUE('<cases><case>example</case></cases>', '/cases/case') AS x1, EXTRACTVALUE('<cases><case>example</case></cases>', '/cases/case/text()') AS x2) as xml", connectionProperties);

    try {
		jdbcDF2.createTempView("xml");
	} catch (AnalysisException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    jdbcDF2.printSchema();
    
    Dataset<Row> namesDF = spark.sql("SELECT * FROM xml");
    namesDF.show();
    
    //jdbcDF2.show();
  }
}
