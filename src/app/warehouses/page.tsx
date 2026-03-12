'use client'

import { useState, useEffect } from 'react'

interface Warehouse {
  id: string
  code: string
  name: string
  address?: string
  status: string
}

export default function WarehouseManagement() {
  const [warehouses, setWarehouses] = useState<Warehouse[]>([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [editingWarehouse, setEditingWarehouse] = useState<Warehouse | null>(null)
  const [formData, setFormData] = useState({
    code: '',
    name: '',
    address: ''
  })

  useEffect(() => {
    fetchWarehouses()
  }, [])

  const fetchWarehouses = async () => {
    try {
      const res = await fetch('/api/warehouses')
      const data = await res.json()
      setWarehouses(data)
    } catch (error) {
      console.error('Failed to fetch warehouses:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      if (editingWarehouse) {
        await fetch(`/api/warehouses/${editingWarehouse.id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(formData)
        })
      } else {
        await fetch('/api/warehouses', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(formData)
        })
      }
      setShowForm(false)
      setEditingWarehouse(null)
      setFormData({ code: '', name: '', address: '' })
      fetchWarehouses()
    } catch (error) {
      console.error('Failed to save warehouse:', error)
    }
  }

  const handleEdit = (warehouse: Warehouse) => {
    setEditingWarehouse(warehouse)
    setFormData({
      code: warehouse.code,
      name: warehouse.name,
      address: warehouse.address || ''
    })
    setShowForm(true)
  }

  const handleDelete = async (id: string) => {
    if (!confirm('确定要删除这个仓库吗？')) return
    try {
      await fetch(`/api/warehouses/${id}`, { method: 'DELETE' })
      fetchWarehouses()
    } catch (error) {
      console.error('Failed to delete warehouse:', error)
    }
  }

  if (loading) {
    return <div style={{ textAlign: 'center', padding: '2rem' }}>加载中...</div>
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h2 style={{ fontSize: '1.5rem', fontWeight: 600, color: '#1e293b' }}>仓库管理</h2>
        <button
          onClick={() => { setShowForm(true); setEditingWarehouse(null); setFormData({ code: '', name: '', address: '' }); }}
          style={{
            padding: '0.75rem 1.5rem',
            background: '#8b5cf6',
            color: 'white',
            border: 'none',
            borderRadius: '8px',
            cursor: 'pointer',
            fontWeight: 500
          }}
        >
          + 添加仓库
        </button>
      </div>

      {/* 表单弹窗 */}
      {showForm && (
        <div style={{
          position: 'fixed',
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          background: 'rgba(0,0,0,0.5)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          zIndex: 1000
        }}>
          <div style={{
            background: 'white',
            borderRadius: '12px',
            padding: '2rem',
            width: '100%',
            maxWidth: '500px'
          }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: 600, marginBottom: '1.5rem' }}>
              {editingWarehouse ? '编辑仓库' : '添加仓库'}
            </h3>
            <form onSubmit={handleSubmit}>
              <div style={{ marginBottom: '1rem' }}>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', fontWeight: 500 }}>仓库编码 *</label>
                <input
                  type="text"
                  value={formData.code}
                  onChange={(e) => setFormData({ ...formData, code: e.target.value })}
                  required
                  style={{ width: '100%', padding: '0.75rem', border: '1px solid #d1d5db', borderRadius: '6px' }}
                />
              </div>
              <div style={{ marginBottom: '1rem' }}>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', fontWeight: 500 }}>仓库名称 *</label>
                <input
                  type="text"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  required
                  style={{ width: '100%', padding: '0.75rem', border: '1px solid #d1d5db', borderRadius: '6px' }}
                />
              </div>
              <div style={{ marginBottom: '1rem' }}>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', fontWeight: 500 }}>仓库地址</label>
                <input
                  type="text"
                  value={formData.address}
                  onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                  style={{ width: '100%', padding: '0.75rem', border: '1px solid #d1d5db', borderRadius: '6px' }}
                />
              </div>
              <div style={{ display: 'flex', gap: '1rem', marginTop: '1.5rem' }}>
                <button type="submit" style={{ flex: 1, padding: '0.75rem', background: '#8b5cf6', color: 'white', border: 'none', borderRadius: '8px', cursor: 'pointer' }}>保存</button>
                <button type="button" onClick={() => { setShowForm(false); setEditingWarehouse(null); }} style={{ flex: 1, padding: '0.75rem', background: '#e2e8f0', color: '#1e293b', border: 'none', borderRadius: '8px', cursor: 'pointer' }}>取消</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* 仓库列表 */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '1rem' }}>
        {warehouses.map((warehouse) => (
          <div key={warehouse.id} style={{
            background: 'white',
            borderRadius: '12px',
            padding: '1.5rem',
            boxShadow: '0 1px 3px rgba(0,0,0,0.1)'
          }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '1rem' }}>
              <div>
                <h3 style={{ fontSize: '1.1rem', fontWeight: 600, color: '#1e293b', marginBottom: '0.25rem' }}>{warehouse.name}</h3>
                <p style={{ fontSize: '0.875rem', color: '#64748b' }}>编码: {warehouse.code}</p>
              </div>
              <span style={{
                padding: '0.25rem 0.75rem',
                borderRadius: '9999px',
                fontSize: '0.75rem',
                background: warehouse.status === 'active' ? '#dcfce7' : '#fee2e2',
                color: warehouse.status === 'active' ? '#166534' : '#991b1b'
              }}>
                {warehouse.status === 'active' ? '正常' : '停用'}
              </span>
            </div>
            <p style={{ fontSize: '0.875rem', color: '#64748b', marginBottom: '1rem' }}>
              📍 {warehouse.address || '暂无地址'}
            </p>
            <div style={{ display: 'flex', gap: '0.5rem' }}>
              <button
                onClick={() => handleEdit(warehouse)}
                style={{ flex: 1, padding: '0.5rem', background: '#f1f5f9', border: 'none', borderRadius: '6px', cursor: 'pointer', fontSize: '0.875rem' }}
              >
                编辑
              </button>
              <button
                onClick={() => handleDelete(warehouse.id)}
                style={{ flex: 1, padding: '0.5rem', background: '#fee2e2', color: '#991b1b', border: 'none', borderRadius: '6px', cursor: 'pointer', fontSize: '0.875rem' }}
              >
                删除
              </button>
            </div>
          </div>
        ))}
        {warehouses.length === 0 && (
          <div style={{
            gridColumn: 'span 3',
            textAlign: 'center',
            padding: '3rem',
            background: 'white',
            borderRadius: '12px',
            color: '#64748b'
          }}>
            暂无仓库数据，点击"添加仓库"创建第一个仓库
          </div>
        )}
      </div>
    </div>
  )
}
