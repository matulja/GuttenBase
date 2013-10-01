package de.akquinet.jbosscc.guttenbase.tools;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.configuration.TestH2ConnectionInfo;
import de.akquinet.jbosscc.guttenbase.sql.SQLLexer;

public class CreateSchemaToolTest extends AbstractGuttenBaseTest
{
  private static final String CONNECTOR_ID = "hsqldb";

  private final CreateSchemaTool _objectUnderTest = new CreateSchemaTool(_connectorRepository);

  @Before
  public void setup() throws Exception
  {
    _connectorRepository.addConnectionInfo(CONNECTOR_ID, new TestH2ConnectionInfo());
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID, "/ddl/tables.sql");
  }

  @Test
  public void testScript() throws Exception
  {
    final List<String> script = _objectUnderTest.createDDLScript(CONNECTOR_ID);
    final List<String> parsedScript = new SQLLexer(script).parse();

    assertTrue(parsedScript.contains("CREATE TABLE FOO_COMPANY ( ID BIGINT NOT NULL,  SUPPLIER CHAR,  NAME VARCHAR )"));
    assertTrue(parsedScript.contains("ALTER TABLE FOO_COMPANY ADD CONSTRAINT PK_FOO_COMPANY_1 PRIMARY KEY (ID)"));
    assertTrue(parsedScript
        .contains("ALTER TABLE FOO_USER_ROLES ADD CONSTRAINT FK_USER_ID_ID_9 FOREIGN KEY (USER_ID) REFERENCES FOO_USER(ID)"));
  }
}