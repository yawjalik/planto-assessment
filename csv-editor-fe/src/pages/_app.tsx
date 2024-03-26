import { Toaster } from 'react-hot-toast';

import GlobalStyles from '@/components/GlobalStyles';

import type { NextPage } from 'next';
import type { AppProps } from 'next/app';
import type { ReactElement, ReactNode } from 'react';

export type NextPageWithLayout<P = NonNullable<unknown>, IP = P> = NextPage<P, IP> & {
  getLayout?: (page: ReactElement) => ReactNode;
};

type AppPropsWithLayout = AppProps & {
  Component: NextPageWithLayout;
};

export default function MyApp({ Component, pageProps }: AppPropsWithLayout) {
  // Use the layout defined at the page level, if available
  const getLayout = Component.getLayout ?? ((page) => page);

  return (
    <>
      <GlobalStyles />
      {getLayout(<Component {...pageProps} />)}
      <Toaster
        toastOptions={{
          position: 'top-right',
        }}
      />
    </>
  );
}
