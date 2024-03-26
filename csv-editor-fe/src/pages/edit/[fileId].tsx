import Link from 'next/link';
import { useRouter } from 'next/router';
import React, { useCallback, useEffect, useRef, useState } from 'react';
import toast from 'react-hot-toast';

import { getCsvById, updateCsv } from '@/api/requests';
import {
  Box,
  Button,
  ButtonContainer,
  Container,
  Header,
  InputButton,
  StyledInput,
} from '@/components/StyledComponents';
import { CsvEntry, CsvFile } from '@/types/csv';

export default function Page() {
  const router = useRouter();
  const [csvFile, setCsvFile] = useState<CsvFile>();
  const [isLoading, setIsLoading] = useState(true);
  const dirtyFieldsRef = useRef<Set<string>>(new Set());

  const handleSubmit = useCallback(
    (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      const { fileId } = router.query;
      if (typeof fileId !== 'string' || !parseInt(fileId)) {
        toast.error('Invalid file ID');
        return;
      }

      const formData = new FormData(e.target as HTMLFormElement);

      const csvEntries: CsvEntry[] = [];
      dirtyFieldsRef.current.forEach((id) => {
        const data = formData.get(id);
        csvEntries.push({ id: parseInt(id), data: data == null ? '' : (data as string) });
      });

      updateCsv(parseInt(fileId), csvEntries)
        .then(() => {
          router.push('/').then(() => {
            toast.success(`Successfully updated ${csvFile?.fileName}`);
          });
        })
        .catch((err) => {
          console.error(err);
          toast.error(`Error updating ${csvFile?.fileName}`);
        });
    },
    [csvFile, router]
  );

  const handleInputChange = (e: React.FormEvent<HTMLInputElement>) => {
    const { id } = e.target as HTMLInputElement;
    const { current: dirtyFields } = dirtyFieldsRef;
    if (!dirtyFields.has(id)) {
      dirtyFields.add(id);
    }
  };

  useEffect(() => {
    if (router.isReady) {
      setIsLoading(true);

      const { fileId } = router.query;
      if (typeof fileId !== 'string' || !parseInt(fileId)) {
        console.error('Invalid file ID');
        toast.error('Invalid file ID');
        setIsLoading(false);
        return;
      }

      getCsvById(parseInt(fileId))
        .then((data) => {
          setCsvFile(data);
        })
        .catch((err) => {
          toast.error('Error fetching file');
          console.error(err);
        })
        .finally(() => {
          setIsLoading(false);
        });
    }
  }, [router]);

  return (
    <Container>
      <Header>Edit CSV</Header>
      {isLoading && <p>Loading...</p>}
      {!isLoading && !csvFile && (
        <div>
          <p>Not found</p>
          <Link href={'/'}>
            <Button>Go back</Button>
          </Link>
        </div>
      )}
      {!isLoading && !!csvFile && (
        <Box>
          <p>{csvFile.fileName}</p>
          <form onSubmit={handleSubmit} autoComplete={'off'}>
            {csvFile.csvRows.map((row, rowNum) => (
              <div key={row.id}>
                {row.csvEntries.map(({ id: entryId, data }) => (
                  <StyledInput
                    key={entryId}
                    id={`${entryId}`}
                    name={`${entryId}`}
                    defaultValue={data}
                    onInput={handleInputChange}
                    width={`${100 / csvFile.csvRows[0].csvEntries.length}%`}
                    fontWeight={rowNum == 0 ? 'bold' : 'normal'}
                  />
                ))}
              </div>
            ))}
            <ButtonContainer>
              <Link href="/">
                <Button>Cancel</Button>
              </Link>
              <Button type={'submit'}>Save and go back</Button>
            </ButtonContainer>
          </form>
        </Box>
      )}
    </Container>
  );
}
