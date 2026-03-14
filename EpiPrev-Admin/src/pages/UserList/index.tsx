import {
  type ActionType,
  PageContainer,
  type ProColumns,
  ProTable,
} from '@ant-design/pro-components';
import { FormattedMessage } from '@umijs/max';
import { Avatar, Button, message, Tag } from 'antd';
import React, { useRef } from 'react';
import type { PageParams } from '@/api/common';
import { pageUser, type SysUser, updateUserStatus } from '@/api/user';

const UserList: React.FC = () => {
  const actionRef = useRef<ActionType>(null);
  const [messageApi, contextHolder] = message.useMessage();

  const handleUpdateStatus = async (id: number, status: string) => {
    try {
      const res = await updateUserStatus(id, status);
      if (res.success) {
        messageApi.success(status === 'AUTH' ? '用户已解冻' : '用户已冻结');
        actionRef.current?.reload();
      } else {
        messageApi.error(res.message || '操作失败');
      }
    } catch (_error) {
      messageApi.error('操作失败');
    }
  };

  const columns: ProColumns<SysUser>[] = [
    {
      title: <FormattedMessage id="pages.userList.avatar" />,
      dataIndex: 'avatar',
      hideInSearch: true,
      render: (_, record) => (
        <Avatar src={record.avatar}>{record.nickName?.[0]}</Avatar>
      ),
    },
    {
      title: <FormattedMessage id="pages.userList.nickName" />,
      dataIndex: 'nickName',
    },
    {
      title: <FormattedMessage id="pages.userList.telephone" />,
      dataIndex: 'telephone',
    },
    {
      title: <FormattedMessage id="pages.userList.email" />,
      dataIndex: 'email',
    },
    {
      title: <FormattedMessage id="pages.userList.role" />,
      dataIndex: 'role',
      valueEnum: {
        ADMIN: {
          text: '管理员',
          color: 'red',
        },
        USER: {
          text: '普通用户',
          color: 'blue',
        },
      },
      render: (role) => {
        if (!role) return '-';
        const roleMap: Record<string, string> = {
          ADMIN: '管理员',
          USER: '普通用户',
        };
        const colorMap: Record<string, string> = {
          ADMIN: 'red',
          USER: 'blue',
        };
        const roleStr = String(role);
        return (
          <Tag color={colorMap[roleStr] || 'default'}>
            {roleMap[roleStr] || role}
          </Tag>
        );
      },
    },
    {
      title: <FormattedMessage id="pages.userList.state" />,
      dataIndex: 'state',
      valueEnum: {
        FROZEN: {
          text: '冻结',
          color: 'red',
        },
        INIT: {
          text: '初始',
          color: 'blue',
        },
        AUTH: {
          text: '已认证',
          color: 'green',
        },
      },
      render: (state) => {
        if (!state) return '-';
        const stateMap: Record<string | number, string> = {
          FROZEN: '冻结',
          INIT: '初始',
          AUTH: '已认证',
        };
        const colorMap: Record<string | number, string> = {
          FROZEN: 'red',
          INIT: 'blue',
          AUTH: 'green',
        };
        return (
          <Tag color={colorMap[state as string | number]}>
            {stateMap[state as string | number] || state}
          </Tag>
        );
      },
    },
    {
      title: <FormattedMessage id="pages.userList.createTime" />,
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: <FormattedMessage id="pages.userList.operating" />,
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => {
        if (record.state === 'INIT') {
          return null;
        }
        const isActive = record.state === 1;
        const _isFrozen = record.state === 'FROZEN' || record.state === 0;
        const isAuth = record.state === 'AUTH';
        return [
          <Button
            key="status"
            type={isActive || isAuth ? 'primary' : 'default'}
            style={{
              backgroundColor: isActive || isAuth ? '#1890ff' : '#fa8c16',
              borderColor: isActive || isAuth ? '#1890ff' : '#fa8c16',
              color: '#fff',
            }}
            size="small"
            onClick={() =>
              record.id != null &&
              handleUpdateStatus(
                record.id,
                isActive || isAuth ? 'FROZEN' : 'AUTH',
              )
            }
          >
            {isActive || isAuth ? '冻结' : '解冻'}
          </Button>,
        ];
      },
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<
        SysUser,
        PageParams & {
          nickName?: string;
          telephone?: string;
          email?: string;
          role?: string;
          state?: string;
        }
      >
        headerTitle={<FormattedMessage id="pages.userList.title" />}
        actionRef={actionRef}
        rowKey="id"
        columns={columns}
        search={{
          labelWidth: 120,
          layout: 'horizontal',
          collapsed: false,
        }}
        request={async (params) => {
          const { nickName, telephone, email, role, state, ...rest } = params;
          const res = await pageUser({
            ...rest,
            keyword: nickName || telephone || email,
            role,
            state,
          } as any);
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
      />
    </PageContainer>
  );
};

export default UserList;
