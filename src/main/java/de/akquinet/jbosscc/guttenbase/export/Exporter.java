package de.akquinet.jbosscc.guttenbase.export;

import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Export schema information and table data to some custom format.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface Exporter {
	/**
	 * Start exporting to a file.
	 */
	void initializeExport(ConnectorRepository connectorRepository, String connectorId, final ExportDumpConnectorInfo exportDumpConnectionInfo)
			throws Exception;

	/**
	 * Finish export
	 */
	void finishExport() throws Exception;

	/**
	 * Write table header when executing an INSERT statement. This is necessary to mark where data for a given table starts since some tables
	 * may be skipped during import. The header is written only once in fact.
	 */
	void writeTableHeader(ExportTableHeader exportTableHeader) throws Exception;

	/**
	 * Dump database information
	 */
	void writeDatabaseMetaData(DatabaseMetaData sourceDatabaseMetaData) throws Exception;

	/**
	 * Called before copying of a table starts.
	 */
	void initializeWriteTableData(TableMetaData tableMetaData) throws Exception;

	/**
	 * Called after copying of a table ends.
	 */
	void finalizeWriteTableData(TableMetaData tableMetaData) throws Exception;

	/**
	 * Called before copying of a table row starts.
	 */
	void initializeWriteRowData(TableMetaData tableMetaData) throws Exception;

	/**
	 * Called after copying of a table row ends.
	 */
	void finalizeWriteRowData(TableMetaData tableMetaData) throws Exception;

	/**
	 * Allow the implementation to flush its buffers. This method is called by {@link ExportDumpConnection#commit()}.
	 */
	void flush() throws Exception;

	void writeDouble(double val) throws Exception;

	void writeObject(Object obj) throws Exception;

	void writeBoolean(boolean val) throws Exception;

	void writeByte(int val) throws Exception;

	void writeShort(int val) throws Exception;

	void writeInt(int val) throws Exception;

	void writeLong(long val) throws Exception;

	void writeFloat(float val) throws Exception;
}
