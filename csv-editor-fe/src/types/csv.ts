export interface CsvEntry {
  id: number;
  data: string;
}

export interface CsvRow {
  id: number;
  csvEntries: CsvEntry[];
}

export interface CsvFileProj {
  id: number;
  fileName: string;
}

export interface CsvFile extends CsvFileProj {
  csvRows: CsvRow[];
}
