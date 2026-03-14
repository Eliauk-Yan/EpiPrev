import {
  type ActionType,
  PageContainer,
  type ProColumns,
  ProTable,
} from '@ant-design/pro-components';
import { FormattedMessage, useIntl } from '@umijs/max';
import { message, Popconfirm, Typography } from 'antd';
import React, { useRef } from 'react';
import { type Comment, pageComment, removeComment } from '@/api/comment';
import type { PageParams } from '@/api/common';

const CommentList: React.FC = () => {
  const actionRef = useRef<ActionType | undefined>(undefined);
  const [messageApi, contextHolder] = message.useMessage();
  const intl = useIntl();

  const handleRemove = async (id: number) => {
    try {
      const res = await removeComment(id);
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

  const columns: ProColumns<Comment>[] = [
    {
      title: <FormattedMessage id="pages.commentList.id" />,
      dataIndex: 'id',
      hideInSearch: true,
      width: 80,
    },
    {
      title: <FormattedMessage id="pages.commentList.postId" />,
      dataIndex: 'postId',
      hideInSearch: true,
      width: 100,
    },
    {
      title: <FormattedMessage id="pages.commentList.userId" />,
      dataIndex: 'userId',
      hideInSearch: true,
      width: 100,
    },
    {
      title: <FormattedMessage id="pages.commentList.content" />,
      dataIndex: 'content',
      ellipsis: true,
      render: (_, record) => (
        <Typography.Paragraph
          ellipsis={{ rows: 2, expandable: true, symbol: '展开' }}
          style={{ marginBottom: 0 }}
        >
          {record.content}
        </Typography.Paragraph>
      ),
    },
    {
      title: <FormattedMessage id="pages.commentList.createTime" />,
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: <FormattedMessage id="pages.userList.operating" />,
      dataIndex: 'option',
      valueType: 'option',
      width: 100,
      render: (_, record) => [
        <Popconfirm
          key="delete"
          title={intl.formatMessage({ id: 'pages.commentList.deleteConfirm' })}
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
      <ProTable<Comment, PageParams>
        headerTitle={<FormattedMessage id="pages.commentList.title" />}
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        request={async (params) => {
          const res = await pageComment(params as any);
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

export default CommentList;
