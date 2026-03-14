import {
  type ActionType,
  ModalForm,
  PageContainer,
  type ProColumns,
  ProForm,
  ProFormDependency,
  type ProFormInstance,
  ProFormRadio,
  ProFormText,
  ProFormTextArea,
  ProTable,
} from '@ant-design/pro-components';
import { FormattedMessage, useIntl } from '@umijs/max';
import { Button, Image, message, Popconfirm, Upload } from 'antd';
import React, { useRef } from 'react';
import {
  type Article,
  addArticle,
  pageArticle,
  removeArticle,
  updateArticle,
  uploadArticleFile,
} from '@/api/article';
import type { PageParams } from '@/api/common';

const ArticleList: React.FC = () => {
  const actionRef = useRef<ActionType | undefined>(undefined);
  const createFormRef = useRef<ProFormInstance<Article> | undefined>(undefined);
  const editFormRef = useRef<ProFormInstance<Article> | undefined>(undefined);
  const [messageApi, contextHolder] = message.useMessage();
  const intl = useIntl();

  const handleAdd = async (fields: Article) => {
    try {
      const res = await addArticle({ ...fields });
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

  const handleUpdate = async (fields: Article) => {
    try {
      const res = await updateArticle({ ...fields });
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
      const res = await removeArticle(id);
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

  const columns: ProColumns<Article>[] = [
    {
      title: <FormattedMessage id="pages.articleList.cover" />,
      dataIndex: 'cover',
      hideInSearch: true,
      render: (_, record) => {
        // 视频：优先显示视频封面（cover），否则用视频本身作为预览
        if (record.type === 1) {
          if (record.cover) {
            return (
              <Image
                src={record.cover}
                width={50}
                height={50}
                style={{ objectFit: 'cover' }}
              />
            );
          }
          if (record.videoUrl) {
            return (
              <video
                src={record.videoUrl}
                style={{
                  width: 50,
                  height: 50,
                  objectFit: 'cover',
                  borderRadius: 4,
                }}
                muted
                controls={false}
              />
            );
          }
        }
        // 图文：正常显示封面图片
        return record.cover ? (
          <Image
            src={record.cover}
            width={50}
            height={50}
            style={{ objectFit: 'cover' }}
          />
        ) : (
          '-'
        );
      },
    },
    {
      title: <FormattedMessage id="pages.articleList.articleTitle" />,
      dataIndex: 'title',
    },
    {
      title: <FormattedMessage id="pages.articleList.category" />,
      dataIndex: 'category',
    },
    {
      title: <FormattedMessage id="pages.articleList.type" />,
      dataIndex: 'type',
      valueType: 'select',
      valueEnum: {
        0: {
          text: intl.formatMessage({ id: 'pages.articleList.image' }),
          status: 'Default',
        },
        1: {
          text: intl.formatMessage({ id: 'pages.articleList.video' }),
          status: 'Processing',
        },
      },
    },
    {
      title: <FormattedMessage id="pages.articleList.views" />,
      dataIndex: 'views',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: <FormattedMessage id="pages.userList.operating" />,
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <ModalForm<Article>
          key="edit"
          title={intl.formatMessage({ id: 'pages.articleList.edit' })}
          trigger={
            <a key="edit">
              <FormattedMessage id="pages.common.edit" />
            </a>
          }
          initialValues={record}
          formRef={editFormRef}
          onFinish={async (value) => {
            const success = await handleUpdate({ ...record, ...value });
            if (success) {
              actionRef.current?.reload();
            }
            return success;
          }}
        >
          <ProFormText
            name="title"
            label={intl.formatMessage({ id: 'pages.articleList.articleTitle' })}
            rules={[{ required: true }]}
          />
          <ProFormText
            name="category"
            label={intl.formatMessage({ id: 'pages.articleList.category' })}
          />
          <ProFormTextArea
            name="summary"
            label={intl.formatMessage({ id: 'pages.articleList.summary' })}
          />
          <ProFormTextArea
            name="content"
            label={intl.formatMessage({ id: 'pages.articleList.content' })}
          />
          <ProFormRadio.Group
            name="type"
            label={intl.formatMessage({ id: 'pages.articleList.type' })}
            options={[
              {
                label: intl.formatMessage({ id: 'pages.articleList.image' }),
                value: 0,
              },
              {
                label: intl.formatMessage({ id: 'pages.articleList.video' }),
                value: 1,
              },
            ]}
          />
          {/* 隐藏真实 URL 字段，由上传组件写入 */}
          <ProFormText name="cover" hidden />
          <ProFormText name="videoUrl" hidden />
          <ProFormDependency name={['type']}>
            {({ type }) => (
              <>
                {type === 0 && (
                  <ProForm.Item
                    label={intl.formatMessage({
                      id: 'pages.articleList.coverUrl',
                    })}
                  >
                    <Upload
                      maxCount={1}
                      beforeUpload={async (file) => {
                        try {
                          const res = await uploadArticleFile(
                            file as File,
                            'cover',
                          );
                          if (res.success && res.data) {
                            editFormRef.current?.setFieldValue(
                              'cover',
                              res.data,
                            );
                            messageApi.success(
                              intl.formatMessage({
                                id: 'pages.articleList.uploadCoverSuccess',
                              }),
                            );
                          } else {
                            messageApi.error(
                              res.message ||
                                intl.formatMessage({
                                  id: 'pages.articleList.uploadCoverFailed',
                                }),
                            );
                          }
                        } catch (_e) {
                          messageApi.error(
                            intl.formatMessage({
                              id: 'pages.articleList.uploadCoverFailed',
                            }),
                          );
                        }
                        return false;
                      }}
                    >
                      <Button type="dashed">
                        <FormattedMessage id="pages.articleList.uploadCover" />
                      </Button>
                    </Upload>
                  </ProForm.Item>
                )}
                {type === 1 && (
                  <ProForm.Item
                    label={intl.formatMessage({
                      id: 'pages.articleList.videoUrl',
                    })}
                  >
                    <Upload
                      maxCount={1}
                      beforeUpload={async (file) => {
                        try {
                          const res = await uploadArticleFile(
                            file as File,
                            'video',
                          );
                          if (res.success && res.data) {
                            editFormRef.current?.setFieldValue(
                              'videoUrl',
                              res.data,
                            );
                            messageApi.success(
                              intl.formatMessage({
                                id: 'pages.articleList.uploadVideoSuccess',
                              }),
                            );
                          } else {
                            messageApi.error(
                              res.message ||
                                intl.formatMessage({
                                  id: 'pages.articleList.uploadVideoFailed',
                                }),
                            );
                          }
                        } catch (_e) {
                          messageApi.error(
                            intl.formatMessage({
                              id: 'pages.articleList.uploadVideoFailed',
                            }),
                          );
                        }
                        return false;
                      }}
                    >
                      <Button type="dashed">
                        <FormattedMessage id="pages.articleList.uploadVideo" />
                      </Button>
                    </Upload>
                  </ProForm.Item>
                )}
              </>
            )}
          </ProFormDependency>
        </ModalForm>,
        <Popconfirm
          key="delete"
          title={intl.formatMessage({ id: 'pages.articleList.deleteConfirm' })}
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
      <ProTable<Article, PageParams>
        headerTitle={<FormattedMessage id="pages.articleList.title" />}
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <ModalForm<Article>
            key="create"
            title={intl.formatMessage({ id: 'pages.articleList.new' })}
            trigger={
              <Button type="primary">
                <FormattedMessage id="pages.articleList.new" />
              </Button>
            }
            formRef={createFormRef}
            onFinish={async (value) => {
              const success = await handleAdd(value);
              if (success) {
                actionRef.current?.reload();
              }
              return success;
            }}
          >
            <ProFormText
              name="title"
              label={intl.formatMessage({
                id: 'pages.articleList.articleTitle',
              })}
              rules={[{ required: true }]}
            />
            <ProFormText
              name="category"
              label={intl.formatMessage({ id: 'pages.articleList.category' })}
            />
            <ProFormTextArea
              name="summary"
              label={intl.formatMessage({ id: 'pages.articleList.summary' })}
            />
            <ProFormTextArea
              name="content"
              label={intl.formatMessage({ id: 'pages.articleList.content' })}
            />
            <ProFormRadio.Group
              name="type"
              label={intl.formatMessage({ id: 'pages.articleList.type' })}
              initialValue={0}
              options={[
                {
                  label: intl.formatMessage({ id: 'pages.articleList.image' }),
                  value: 0,
                },
                {
                  label: intl.formatMessage({ id: 'pages.articleList.video' }),
                  value: 1,
                },
              ]}
            />
            {/* 隐藏真实 URL 字段，由上传组件写入 */}
            <ProFormText name="cover" hidden />
            <ProFormText name="videoUrl" hidden />
            <ProFormDependency name={['type']}>
              {({ type }) => (
                <>
                  {(type === 0 || type === undefined) && (
                    <ProForm.Item
                      label={intl.formatMessage({
                        id: 'pages.articleList.coverUrl',
                      })}
                    >
                      <Upload
                        maxCount={1}
                        beforeUpload={async (file) => {
                          try {
                            const res = await uploadArticleFile(
                              file as File,
                              'cover',
                            );
                            if (res.success && res.data) {
                              createFormRef.current?.setFieldValue(
                                'cover',
                                res.data,
                              );
                              messageApi.success(
                                intl.formatMessage({
                                  id: 'pages.articleList.uploadCoverSuccess',
                                }),
                              );
                            } else {
                              messageApi.error(
                                res.message ||
                                  intl.formatMessage({
                                    id: 'pages.articleList.uploadCoverFailed',
                                  }),
                              );
                            }
                          } catch (_e) {
                            messageApi.error(
                              intl.formatMessage({
                                id: 'pages.articleList.uploadCoverFailed',
                              }),
                            );
                          }
                          return false;
                        }}
                      >
                        <Button type="dashed">
                          <FormattedMessage id="pages.articleList.uploadCover" />
                        </Button>
                      </Upload>
                    </ProForm.Item>
                  )}
                  {type === 1 && (
                    <ProForm.Item
                      label={intl.formatMessage({
                        id: 'pages.articleList.videoUrl',
                      })}
                    >
                      <Upload
                        maxCount={1}
                        beforeUpload={async (file) => {
                          try {
                            const res = await uploadArticleFile(
                              file as File,
                              'video',
                            );
                            if (res.success && res.data) {
                              createFormRef.current?.setFieldValue(
                                'videoUrl',
                                res.data,
                              );
                              messageApi.success(
                                intl.formatMessage({
                                  id: 'pages.articleList.uploadVideoSuccess',
                                }),
                              );
                            } else {
                              messageApi.error(
                                res.message ||
                                  intl.formatMessage({
                                    id: 'pages.articleList.uploadVideoFailed',
                                  }),
                              );
                            }
                          } catch (_e) {
                            messageApi.error(
                              intl.formatMessage({
                                id: 'pages.articleList.uploadVideoFailed',
                              }),
                            );
                          }
                          return false;
                        }}
                      >
                        <Button type="dashed">
                          <FormattedMessage id="pages.articleList.uploadVideo" />
                        </Button>
                      </Upload>
                    </ProForm.Item>
                  )}
                </>
              )}
            </ProFormDependency>
          </ModalForm>,
        ]}
        request={async (params) => {
          const res = await pageArticle(params as any);
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

export default ArticleList;
