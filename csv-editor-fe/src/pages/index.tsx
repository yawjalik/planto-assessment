import axios from 'axios';
import Link from 'next/link';
import React, { useCallback, useEffect, useState } from 'react';
import toast from 'react-hot-toast';

import { getCsvs } from '@/api/requests';
import { Box, Button, ButtonContainer, Container, CsvRow, Header, InputButton } from '@/components/StyledComponents';
import { CsvFileProj } from '@/types/csv';

const CsvEditorPage = () => {
  const [csvFiles, setCsvFiles] = useState<CsvFileProj[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const handleUpload = useCallback(
    async (e: React.FormEvent<HTMLInputElement>) => {
      const { files } = e.target as globalThis.EventTarget & { files: FileList };
      const file = files[0];

      const body = new FormData();
      body.append('file', file);

      try {
        const res = await axios.post('http://localhost:8080/v1/api/csv', body);
        setCsvFiles([...csvFiles, res.data as CsvFileProj]);
        toast.success(`Successfully uploaded ${file.name}`);
      } catch (e) {
        console.error(e);
        toast.error('Error uploading file');
      }
    },
    [csvFiles]
  );

  useEffect(() => {
    setIsLoading(true);
    getCsvs()
      .then((data) => setCsvFiles(data))
      .catch((err) => {
        console.error(err);
        toast.error('Error fetching files');
      })
      .finally(() => {
        setIsLoading(false);
      });
  }, []);

  return (
    <Container>
      <Header>Manage CSVs</Header>
      {isLoading ? (
        <p>Loading...</p>
      ) : (
        <Box>
          {csvFiles.length == 0 ? (
            <p>Start by uploading a file</p>
          ) : (
            <>
              <p>Filename</p>
              {csvFiles.map(({ id, fileName }) => (
                <CsvRow key={id}>
                  <p>{fileName}</p>
                  <Link href={`/edit/${id}`}>
                    <Button>Edit</Button>
                  </Link>
                </CsvRow>
              ))}
            </>
          )}
          <ButtonContainer>
            <InputButton id={'upload-btn'} type={'file'} InputProps={{ onInput: handleUpload }}>
              Upload CSV
            </InputButton>
          </ButtonContainer>
        </Box>
      )}
    </Container>
  );
};

export default CsvEditorPage;
