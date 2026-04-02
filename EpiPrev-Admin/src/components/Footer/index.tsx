import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';

const Footer: React.FC = () => {
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright="Powered by ZhangBo"
      links={[
        {
          key: 'ZhangBo',
          title: 'ZhangBo',
          href: 'https://github.com/Eliauk-Yan/EpiPrev.git',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <GithubOutlined />,
          href: 'https://github.com/Eliauk-Yan/EpiPrev.git',
          blankTarget: true,
        },
      ]}
    />
  );
};

export default Footer;
