import axios from 'axios';

import { CsvEntry, CsvFile, CsvFileProj } from '@/types/csv';

export const getCsvs = async () => {
  const res = await axios.get('http://localhost:8080/v1/api/csv');
  return res.data as CsvFileProj[];
};

export const getCsvById = async (id: number) => {
  const res = await axios.get(`http://localhost:8080/v1/api/csv/${id}`);
  return res.data as CsvFile;
};

export const updateCsv = async (id: number, csvEntries: CsvEntry[]) => {
  await axios.put(`http://localhost:8080/v1/api/csv/${id}`, csvEntries);
};
