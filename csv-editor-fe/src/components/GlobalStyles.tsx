import { css, Global } from '@emotion/react';

const GlobalStyles = () => (
  <Global
    styles={css`
      * {
        font-family: Verdana, sans-serif;
      }
    `}
  />
);

export default GlobalStyles;
