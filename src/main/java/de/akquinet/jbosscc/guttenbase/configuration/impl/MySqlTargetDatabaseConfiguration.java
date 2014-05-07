package de.akquinet.jbosscc.guttenbase.configuration.impl;

import java.sql.Connection;
import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Implementation for MYSQL data base.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class MySqlTargetDatabaseConfiguration extends DefaultTargetDatabaseConfiguration
{
  private final int _caseSensitivityMode;
  private final boolean _disableUniqueChecks;

  /**
   * @param connectorRepository
   * @param disableUniqueChecks
   *          disable unique checks, too. Not just foreign key constraints.
   * @param caseSensitivityMode
   *          See {@link http://dev.mysql.com/doc/refman/5.5/en/server-system-variables.html#sysvar_lower_case_table_names} for
   *          details. A value < 0 means do not alter the value
   */
  public MySqlTargetDatabaseConfiguration(
      final ConnectorRepository connectorRepository,
      final boolean disableUniqueChecks,
      final int caseSensitivityMode)
  {
    super(connectorRepository);

    _disableUniqueChecks = disableUniqueChecks;
    _caseSensitivityMode = caseSensitivityMode;
  }

  public MySqlTargetDatabaseConfiguration(final ConnectorRepository connectorRepository)
  {
    this(connectorRepository, false, -1);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeTargetConnection(final Connection connection, final String connectorId) throws SQLException
  {
    if (connection.getAutoCommit())
    {
      connection.setAutoCommit(false);
    }

    setReferentialIntegrity(connection, false);

    if (_disableUniqueChecks)
    {
      setUniqueChecks(connection, false);
    }

    if (_caseSensitivityMode >= 0)
    {
      setCaseSensitivityMode(connection);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void finalizeTargetConnection(final Connection connection, final String connectorId) throws SQLException
  {
    setReferentialIntegrity(connection, true);

    if (_disableUniqueChecks)
    {
      setUniqueChecks(connection, true);
    }

    if (_caseSensitivityMode >= 0)
    {
      resetCaseSensitivityMode(connection);
    }
  }

  private void setReferentialIntegrity(final Connection connection, final boolean enable) throws SQLException
  {
    executeSQL(connection, "SET FOREIGN_KEY_CHECKS = " + (enable ? "1" : "0") + ";");
  }

  private void setUniqueChecks(final Connection connection, final boolean enable) throws SQLException
  {
    executeSQL(connection, "SET UNIQUE_CHECKS = " + (enable ? "1" : "0") + ";");
  }

  private void setCaseSensitivityMode(final Connection connection) throws SQLException
  {
    executeSQL(connection,
        "SET @OLD_LOWER_CASE_TABLE_NAMES=@@LOWER_CASE_TABLE_NAMES, LOWER_CASE_TABLE_NAMES = " + _caseSensitivityMode + ";");
  }

  private void resetCaseSensitivityMode(final Connection connection) throws SQLException
  {
    executeSQL(connection, "SET LOWER_CASE_TABLE_NAMES=@OLD_LOWER_CASE_TABLE_NAMES;");
  }
}
