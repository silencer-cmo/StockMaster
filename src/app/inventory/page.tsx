'use client'

import { useState, useEffect } from 'react'

interface Inventory {
  id: string
  productId: string
  warehouseId: string
  quantity: number
  minQuantity: number
  maxQuantity?: number
  status: string
  product?: { name: string; sku: string }
  warehouse?: { name: string }
}

interface Product {
  id: string
  name: string
  sku: string
}

interface Warehouse {
  id: string
  name: string
  code: string
}

interface InventoryManagementProps {
  openAddForm?: boolean
  onFormOpened?: () => void
}

export default function InventoryManagement({ openAddForm, onFormOpened }: InventoryManagementProps) {
  const [inventory, setInventory] = useState<Inventory[]>([])
  const [products, setProducts] = useState<Product[]>([])
  const [warehouses, setWarehouses] = useState<Warehouse[]>([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState({
    productId: '',
    warehouseId: '',
    quantity: 0,
    minQuantity: 0,
    maxQuantity: 0
  })

  useEffect(() => {
    fetchData()
  }, [])

  // 处理从快捷操作传来的打开表单请求
  useEffect(() => {
    if (openAddForm && !showForm) {
      setShowForm(true)
      setFormData({ productId: '', warehouseId: '', quantity: 0, minQuantity: 0, maxQuantity: 0 })
      if (onFormOpened) {
        onFormOpened()
      }
    }
  }, [openAddForm, onFormOpened, showForm])

  const fetchData = async () => {
    try {
      const [invRes, prodRes, wareRes] = await Promise.all([
        fetch('/api/inventory'),
        fetch('/api/products'),
        fetch('/api/warehouses')
      ])
      setInventory(await invRes.json())
      setProducts(await prodRes.json())
      setWarehouses(await wareRes.json())
    } catch (error) {
      console.error('Failed to fetch data:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      await fetch('/api/inventory', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      })
      setShowForm(false)
      setFormData({ productId: '', warehouseId: '', quantity: 0, minQuantity: 0, maxQuantity: 0 })
      fetchData()
    } catch (error) {
      console.error('Failed to save inventory:', error)
    }
  }

  const getProductName = (productId: string) => {
    const product = products.find(p => p.id === productId)
    return product ? `${product.sku} - ${product.name}` : productId
  }

  const getWarehouseName = (warehouseId: string) => {
    const warehouse = warehouses.find(w => w.id === warehouseId)
    return warehouse ? warehouse.name : warehouseId
  }

  const getStatusBadge = (status: string, quantity: number, minQuantity: number) => {
    if (quantity < minQuantity) {
      return { text: '库存不足', bg: '#fee2e2', color: '#991b1b' }
    }
    switch (status) {
      case 'low': return { text: '库存偏低', bg: '#fef3c7', color: '#92400e' }
      case 'overstock': return { text: '库存过量', bg: '#dbeafe', color: '#1e40af' }
      default: return { text: '正常', bg: '#dcfce7', color: '#166534' }
    }
  }

  if (loading) {
    return <div style={{ textAlign: 'center', padding: '2rem' }}>加载中...</div>
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h2 style={{ fontSize: '1.5rem', fontWeight: 600, color: '#1e293b' }}>库存管理</h2>
        <button
          onClick={() => setShowForm(true)}
          style={{
            padding: '0.75rem 1.5rem',
            background: '#10b981',
            color: 'white',
            border: 'none',
            borderRadius: '8px',
            cursor: 'pointer',
            fontWeight: 500
          }}
        >
          + 添加库存
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
            <h3 style={{ fontSize: '1.25rem', fontWeight: 600, marginBottom: '1.5rem' }}>添加库存</h3>
            <form onSubmit={handleSubmit}>
              <div style={{ marginBottom: '1rem' }}>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', fontWeight: 500 }}>产品 *</label>
                <select
                  value={formData.productId}
                  onChange={(e) => setFormData({ ...formData, productId: e.target.value })}
                  required
                  style={{ width: '100%', padding: '0.75rem', border: '1px solid #d1d5db', borderRadius: '6px' }}
                >
                  <option value="">选择产品</option>
                  {products.map(p => <option key={p.id} value={p.id}>{p.sku} - {p.name}</option>)}
                </select>
              </div>
              <div style={{ marginBottom: '1rem' }}>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', fontWeight: 500 }}>仓库 *</label>
                <select
                  value={formData.warehouseId}
                  onChange={(e) => setFormData({ ...formData, warehouseId: e.target.value })}
                  required
                  style={{ width: '100%', padding: '0.75rem', border: '1px solid #d1d5db', borderRadius: '6px' }}
                >
                  <option value="">选择仓库</option>
                  {warehouses.map(w => <option key={w.id} value={w.id}>{w.name}</option>)}
                </select>
              </div>
              <div style={{ marginBottom: '1rem' }}>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', fontWeight: 500 }}>库存数量 *</label>
                <input
                  type="number"
                  value={formData.quantity}
                  onChange={(e) => setFormData({ ...formData, quantity: parseInt(e.target.value) || 0 })}
                  required
                  style={{ width: '100%', padding: '0.75rem', border: '1px solid #d1d5db', borderRadius: '6px' }}
                />
              </div>
              <div style={{ marginBottom: '1rem' }}>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', fontWeight: 500 }}>最低库存</label>
                <input
                  type="number"
                  value={formData.minQuantity}
                  onChange={(e) => setFormData({ ...formData, minQuantity: parseInt(e.target.value) || 0 })}
                  style={{ width: '100%', padding: '0.75rem', border: '1px solid #d1d5db', borderRadius: '6px' }}
                />
              </div>
              <div style={{ display: 'flex', gap: '1rem', marginTop: '1.5rem' }}>
                <button type="submit" style={{ flex: 1, padding: '0.75rem', background: '#10b981', color: 'white', border: 'none', borderRadius: '8px', cursor: 'pointer' }}>保存</button>
                <button type="button" onClick={() => setShowForm(false)} style={{ flex: 1, padding: '0.75rem', background: '#e2e8f0', color: '#1e293b', border: 'none', borderRadius: '8px', cursor: 'pointer' }}>取消</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* 库存列表 */}
      <div style={{ background: 'white', borderRadius: '12px', boxShadow: '0 1px 3px rgba(0,0,0,0.1)', overflow: 'hidden' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ background: '#f8fafc' }}>
              <Th>产品</Th>
              <Th>仓库</Th>
              <Th>库存数量</Th>
              <Th>最低库存</Th>
              <Th>状态</Th>
            </tr>
          </thead>
          <tbody>
            {inventory.map((item) => {
              const badge = getStatusBadge(item.status, item.quantity, item.minQuantity)
              return (
                <tr key={item.id} style={{ borderBottom: '1px solid #e2e8f0' }}>
                  <Td>{getProductName(item.productId)}</Td>
                  <Td>{getWarehouseName(item.warehouseId)}</Td>
                  <Td>
                    <span style={{ fontWeight: 600, color: item.quantity < item.minQuantity ? '#dc2626' : '#1e293b' }}>
                      {item.quantity}
                    </span>
                  </Td>
                  <Td>{item.minQuantity}</Td>
                  <Td>
                    <span style={{
                      padding: '0.25rem 0.75rem',
                      borderRadius: '9999px',
                      fontSize: '0.75rem',
                      background: badge.bg,
                      color: badge.color
                    }}>
                      {badge.text}
                    </span>
                  </Td>
                </tr>
              )
            })}
            {inventory.length === 0 && (
              <tr>
                <Td colSpan={5} style={{ textAlign: 'center', padding: '2rem', color: '#64748b' }}>
                  暂无库存数据
                </Td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}

function Th({ children }: { children: React.ReactNode }) {
  return <th style={{ padding: '1rem', textAlign: 'left', fontSize: '0.875rem', fontWeight: 600, color: '#475569' }}>{children}</th>
}

function Td({ children, colSpan }: { children: React.ReactNode; colSpan?: number }) {
  return <td style={{ padding: '1rem', fontSize: '0.875rem', color: '#1e293b' }} colSpan={colSpan}>{children}</td>
}
