import {
  type ActionType,
  ModalForm,
  PageContainer,
  type ProColumns,
  ProFormDateTimePicker,
  type ProFormInstance,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProTable,
} from '@ant-design/pro-components';
import { FormattedMessage, useIntl } from '@umijs/max';
import { Button, message, Popconfirm } from 'antd';
import React, { useRef } from 'react';
import type { PageParams } from '@/api/common';
import {
  addNews,
  type News,
  pageNews,
  removeNews,
  updateNews,
} from '@/api/news';

const NewsList: React.FC = () => {
  const actionRef = useRef<ActionType | undefined>(undefined);
  const createFormRef = useRef<ProFormInstance<News> | undefined>(undefined);
  const editFormRef = useRef<ProFormInstance<News> | undefined>(undefined);
  const [messageApi, contextHolder] = message.useMessage();
  const intl = useIntl();

  const handleAdd = async (fields: News) => {
    try {
      const res = await addNews({ ...fields });
      if (res.success) {
        messageApi.success(
          intl.formatMessage({ id: 'pages.common.addSuccess' }),
        );
        return true;
      }
      messageApi.error(
        res.message || intl.formatMessage({ id: 'pages.common.addFailed' }),
      );
      return false;
    } catch (_error) {
      messageApi.error(intl.formatMessage({ id: 'pages.common.addFailed' }));
      return false;
    }
  };

  const handleUpdate = async (fields: News) => {
    try {
      const res = await updateNews({ ...fields });
      if (res.success) {
        messageApi.success(
          intl.formatMessage({ id: 'pages.common.updateSuccess' }),
        );
        return true;
      }
      messageApi.error(
        res.message || intl.formatMessage({ id: 'pages.common.updateFailed' }),
      );
      return false;
    } catch (_error) {
      messageApi.error(intl.formatMessage({ id: 'pages.common.updateFailed' }));
      return false;
    }
  };

  const handleRemove = async (id: number) => {
    try {
      const res = await removeNews(id);
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

  const columns: ProColumns<News>[] = [
    {
      title: <FormattedMessage id="pages.newsList.newsTitle" />,
      dataIndex: 'title',
    },
    {
      title: <FormattedMessage id="pages.newsList.summary" />,
      dataIndex: 'summary',
      hideInSearch: true,
      ellipsis: true,
    },
    {
      title: <FormattedMessage id="pages.newsList.source" />,
      dataIndex: 'source',
    },
    {
      title: <FormattedMessage id="pages.newsList.level" />,
      dataIndex: 'level',
      valueType: 'select',
      valueEnum: {
        normal: {
          text: intl.formatMessage({ id: 'pages.newsList.level.normal' }),
          status: 'Default',
        },
        important: {
          text: intl.formatMessage({ id: 'pages.newsList.level.important' }),
          status: 'Warning',
        },
        urgent: {
          text: intl.formatMessage({ id: 'pages.newsList.level.urgent' }),
          status: 'Error',
        },
      },
    },
    {
      title: <FormattedMessage id="pages.newsList.publishTime" />,
      dataIndex: 'publishTime',
      hideInSearch: true,
      valueType: 'dateTime',
    },
    {
      title: <FormattedMessage id="pages.userList.operating" />,
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <ModalForm<News>
          key="edit"
          title={intl.formatMessage({ id: 'pages.newsList.edit' })}
          trigger={
            <a key="edit">
              <FormattedMessage id="pages.common.edit" />
            </a>
          }
          initialValues={{
            ...record,
            publishTime: record.publishTime
              ? new Date(record.publishTime)
              : undefined,
          }}
          formRef={editFormRef}
          onFinish={async (value) => {
            const success = await handleUpdate({
              ...record,
              ...value,
              publishTime: value.publishTime
                ? new Date(value.publishTime).toISOString()
                : record.publishTime,
            });
            if (success) {
              actionRef.current?.reload();
            }
            return success;
          }}
        >
          <ProFormText
            name="title"
            label={intl.formatMessage({ id: 'pages.newsList.newsTitle' })}
            rules={[{ required: true }]}
          />
          <ProFormTextArea
            name="summary"
            label={intl.formatMessage({ id: 'pages.newsList.summary' })}
          />
          <ProFormText
            name="source"
            label={intl.formatMessage({ id: 'pages.newsList.source' })}
          />
          <ProFormSelect
            name="level"
            label={intl.formatMessage({ id: 'pages.newsList.level' })}
            options={[
              {
                label: intl.formatMessage({
                  id: 'pages.newsList.level.normal',
                }),
                value: 'normal',
              },
              {
                label: intl.formatMessage({
                  id: 'pages.newsList.level.important',
                }),
                value: 'important',
              },
              {
                label: intl.formatMessage({
                  id: 'pages.newsList.level.urgent',
                }),
                value: 'urgent',
              },
            ]}
          />
          <ProFormDateTimePicker
            name="publishTime"
            label={intl.formatMessage({ id: 'pages.newsList.publishTime' })}
          />
          <ProFormTextArea
            name="content"
            label={intl.formatMessage({ id: 'pages.newsList.content' })}
          />
        </ModalForm>,
        <Popconfirm
          key="delete"
          title={intl.formatMessage({ id: 'pages.newsList.deleteConfirm' })}
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
      <ProTable<News, PageParams>
        headerTitle={<FormattedMessage id="pages.newsList.title" />}
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <ModalForm<News>
            key="create"
            title={intl.formatMessage({ id: 'pages.newsList.new' })}
            trigger={
              <Button type="primary">
                <FormattedMessage id="pages.newsList.new" />
              </Button>
            }
            formRef={createFormRef}
            onFinish={async (value) => {
              const success = await handleAdd({
                ...value,
                publishTime: value.publishTime
                  ? new Date(value.publishTime).toISOString()
                  : undefined,
              });
              if (success) {
                actionRef.current?.reload();
              }
              return success;
            }}
          >
            <ProFormText
              name="title"
              label={intl.formatMessage({ id: 'pages.newsList.newsTitle' })}
              rules={[{ required: true }]}
            />
            <ProFormTextArea
              name="summary"
              label={intl.formatMessage({ id: 'pages.newsList.summary' })}
            />
            <ProFormText
              name="source"
              label={intl.formatMessage({ id: 'pages.newsList.source' })}
            />
            <ProFormSelect
              name="level"
              label={intl.formatMessage({ id: 'pages.newsList.level' })}
              options={[
                {
                  label: intl.formatMessage({
                    id: 'pages.newsList.level.normal',
                  }),
                  value: 'normal',
                },
                {
                  label: intl.formatMessage({
                    id: 'pages.newsList.level.important',
                  }),
                  value: 'important',
                },
                {
                  label: intl.formatMessage({
                    id: 'pages.newsList.level.urgent',
                  }),
                  value: 'urgent',
                },
              ]}
            />
            <ProFormDateTimePicker
              name="publishTime"
              label={intl.formatMessage({ id: 'pages.newsList.publishTime' })}
            />
            <ProFormTextArea
              name="content"
              label={intl.formatMessage({ id: 'pages.newsList.content' })}
            />
          </ModalForm>,
        ]}
        request={async (params) => {
          const { title, source, level, ...rest } = params as any;
          const res = await pageNews({
            ...(rest as any),
            word: title || source,
            level,
          });
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

export default NewsList;
