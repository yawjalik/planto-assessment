import styled from '@emotion/styled';
import React, { FC, PropsWithChildren } from 'react';

interface InputButtonProps extends PropsWithChildren {
  id: string;
  type: string;
  ButtonProps?: React.ButtonHTMLAttributes<any>;
  InputProps?: React.InputHTMLAttributes<any>;
}

interface StyledInputProps {
  width?: string;
  fontWeight?: string;
}

export const Header = styled.h1`
  font-weight: normal;
`;

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

export const Box = styled.div`
  border: 1px solid #777;
  padding: 16px;
  min-width: 80%;
  border-radius: 4px;
`;

export const CsvRow = styled.div`
  display: flex;
  margin: 10px 0;
  padding: 4px 10px;
  justify-content: space-between;
  align-items: center;
  border: 1px solid #777;
  border-radius: 4px;
`;

export const ButtonContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
`;

export const StyledInput = styled.input<StyledInputProps>`
  padding: 10px 12px 10px 4px;
  border-radius: 2px;
  margin: -0.5px;
  border: 1px solid #777;
  box-sizing: border-box;
  width: ${(props) => props.width || 'auto'};
  font-weight: ${(props) => props.fontWeight || 'normal'};
`;

const BaseButton = styled.button`
  font-size: 1rem;
  color: white;
  padding: 0;
  box-sizing: border-box;
  background: mediumpurple;
  border-radius: 4px;
  border-style: unset;
  &:hover {
    background: rebeccapurple;
    border-color: rebeccapurple;
    cursor: pointer;
  }
`;

export const Button = styled(BaseButton)`
  padding: 8px 10px;
`;

export const InputButton: FC<InputButtonProps> = ({ id, type, ButtonProps, InputProps, children }) => {
  const Label = styled.label`
    display: block;
    cursor: pointer;
    width: 100%;
    height: 100%;
    padding: 10px;
    box-sizing: border-box;
  `;

  return (
    <BaseButton {...ButtonProps}>
      <input id={id} type={type} hidden {...InputProps} />
      <Label htmlFor={id}>{children}</Label>
    </BaseButton>
  );
};
