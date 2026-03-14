import {
  type ActionType,
  PageContainer,
  type ProColumns,
  ProTable,
} from '@ant-design/pro-components';
import { FormattedMessage, useIntl } from '@umijs/max';
import { message, Popconfirm, Typography } from 'antd';
import React, { useRef } from 'react';
import type { PageParams } from '@/api/common';
import { type ForumPost, pageForum, removeForum } from '@/api/forum';

const { Paragraph } = Typography;

const ForumList: React.FC = () => {
  const actionRef = useRef<ActionType | undefined>(undefined);
  const [messageApi, contextHolder] = message.useMessage();
  const intl = useIntl();

  const handleRemove = async (id: number) => {
    try {
      const res = await removeForum(id);
      if (res.success) {
        messageApi.success(
          intl.formatMessage({ id: 'pages.common.deleteSuccess' }),
        );
        actionRef.current?.reload();
        return;
      }
      messageApi.error(
        res.message || intl.formatMessage({ id: 'pages.common.deleteFailed' }),
      );
    } catch (_error) {
      messageApi.error(intl.formatMessage({ id: 'pages.common.deleteFailed' }));
    }
  };

  const columns: ProColumns<ForumPost>[] = [
    {
      title: <FormattedMessage id="pages.forumList.postTitle" />,
      dataIndex: 'title',
      ellipsis: true,
    },
    {
      title: <FormattedMessage id="pages.forumList.content" />,
      dataIndex: 'content',
      hideInSearch: true,
      ellipsis: true,
      width: 300,
      render: (_, record) => (
        <Paragraph
          ellipsis={{ rows: 2, expandable: false }}
          style={{ marginBottom: 0 }}
        >
          {record.content || '-'}
        </Paragraph>
      ),
    },
    {
      title: <FormattedMessage id="pages.forumList.username" />,
      dataIndex: 'username',
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="pages.forumList.userId" />,
      dataIndex: 'userId',
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="pages.forumList.views" />,
      dataIndex: 'views',
      hideInSearch: true,
      sorter: (a, b) => (a.views || 0) - (b.views || 0),
    },
    {
      title: <FormattedMessage id="pages.forumList.createTime" />,
      dataIndex: 'createTime',
      hideInSearch: true,
      valueType: 'dateTime',
    },
    {
      title: <FormattedMessage id="pages.userList.operating" />,
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <Popconfirm
          key="delete"
          title={intl.formatMessage({ id: 'pages.forumList.deleteConfirm' })}
          onConfirm={() => record.id != null && handleRemove(record.id)}
          okText={<FormattedMessage id="pages.common.ok" />}
          cancelText={<FormattedMessage id="pages.common.cancel" />}
        >
          <a style={{ color: 'red' }}>
            <FormattedMessage id="pages.common.delete" />
          </a>
        </Popconfirm>,
      ],
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<ForumPost, PageParams>
        headerTitle={<FormattedMessage id="pages.forumList.title" />}
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        request={async (params) => {
          const res = await pageForum(params as any);
          if (res.success) {
            return {
              data: res.data,
              success: true,
              total: res.total,
            };
          }
          return {
            data: [],
            success: false,
            total: 0,
          };
        }}
        columns={columns}
      />
    </PageContainer>
  );
};

export default ForumList;
