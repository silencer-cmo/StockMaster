'use client'

import { useState, useEffect } from 'react'

interface Product {
  id: string
  sku: string
  name: string
  description?: string
  category?: string
  unit: string
  price: number
  status: string
}

export default function ProductManagement() {
  const [products, setProducts] = useState<Product[]>([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [editingProduct, setEditingProduct] = useState<Product | null>(null)
  const [formData, setFormData] = useState({
    sku: '',
    name: '',
    description: '',
    category: '',
    unit: '件',
    price: 0
  })

  useEffect(() => {
    fetchProducts()
  }, [])

  const fetchProducts = async () => {
    try {
      const res = await fetch('/api/products')
      const data = await res.json()
      setProducts(data)
    } catch (error) {
      console.error('Failed to fetch products:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      if (editingProduct) {
        await fetch(`/api/products/${editingProduct.id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(formData)
        })
      } else {
        await fetch('/api/products', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(formData)
        })
      }
      setShowForm(false)
      setEditingProduct(null)
      setFormData({ sku: '', name: '', description: '', category: '', unit: '件', price: 0 })
      fetchProducts()
    } catch (error) {
      console.error('Failed to save product:', error)
    }
  }

  const handleEdit = (product: Product) => {
    setEditingProduct(product)
    setFormData({
      sku: product.sku,
      name: product.name,
      description: product.description || '',
      category: product.category || '',
      unit: product.unit,
      price: product.price
    })
    setShowForm(true)
  }

  const handleDelete = async (id: string) => {
    if (!confirm('确定要删除这个产品吗？')) return
    try {
      await fetch(`/api/products/${id}`, { method: 'DELETE' })
      fetchProducts()
    } catch (error) {
      console.error('Failed to delete product:', error)
    }
  }

  if (loading) {
    return <div style={{ textAlign: 'center', padding: '2rem' }}>加载中...</div>
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h2 style={{ fontSize: '1.5rem', fontWeight: 600, color: '#1e293b' }}>产品管理</h2>
        <button
          onClick={() => { setShowForm(true); setEditingProduct(null); setFormData({ sku: '', name: '', description: '', category: '', unit: '件', price: 0 }); }}
          style={{
            padding: '0.75rem 1.5rem',
            background: '#3b82f6',
            color: 'white',
            border: 'none',
            borderRadius: '8px',
            cursor: 'pointer',
            fontWeight: 500
          }}
        >
          + 添加产品
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
            maxWidth: '500px',
            maxHeight: '90vh',
            overflow: 'auto'
          }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: 600, marginBottom: '1.5rem' }}>
              {editingProduct ? '编辑产品' : '添加产品'}
            </h3>
            <form onSubmit={handleSubmit}>
              <FormField label="产品编码 *" value={formData.sku} onChange={(v) => setFormData({ ...formData, sku: v })} required />
              <FormField label="产品名称 *" value={formData.name} onChange={(v) => setFormData({ ...formData, name: v })} required />
              <FormField label="产品描述" value={formData.description} onChange={(v) => setFormData({ ...formData, description: v })} />
              <FormField label="产品分类" value={formData.category} onChange={(v) => setFormData({ ...formData, category: v })} />
              <FormField label="计量单位" value={formData.unit} onChange={(v) => setFormData({ ...formData, unit: v })} />
              <FormField label="单价" type="number" value={formData.price.toString()} onChange={(v) => setFormData({ ...formData, price: parseFloat(v) || 0 })} />
              <div style={{ display: 'flex', gap: '1rem', marginTop: '1.5rem' }}>
                <button type="submit" style={{ flex: 1, padding: '0.75rem', background: '#3b82f6', color: 'white', border: 'none', borderRadius: '8px', cursor: 'pointer' }}>
                  保存
                </button>
                <button type="button" onClick={() => { setShowForm(false); setEditingProduct(null); }} style={{ flex: 1, padding: '0.75rem', background: '#e2e8f0', color: '#1e293b', border: 'none', borderRadius: '8px', cursor: 'pointer' }}>
                  取消
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* 产品列表 */}
      <div style={{ background: 'white', borderRadius: '12px', boxShadow: '0 1px 3px rgba(0,0,0,0.1)', overflow: 'hidden' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ background: '#f8fafc' }}>
              <Th>编码</Th>
              <Th>名称</Th>
              <Th>分类</Th>
              <Th>单位</Th>
              <Th>单价</Th>
              <Th>状态</Th>
              <Th>操作</Th>
            </tr>
          </thead>
          <tbody>
            {products.map((product) => (
              <tr key={product.id} style={{ borderBottom: '1px solid #e2e8f0' }}>
                <Td>{product.sku}</Td>
                <Td>{product.name}</Td>
                <Td>{product.category || '-'}</Td>
                <Td>{product.unit}</Td>
                <Td>¥{product.price.toFixed(2)}</Td>
                <Td>
                  <span style={{
                    padding: '0.25rem 0.75rem',
                    borderRadius: '9999px',
                    fontSize: '0.75rem',
                    background: product.status === 'active' ? '#dcfce7' : '#fee2e2',
                    color: product.status === 'active' ? '#166534' : '#991b1b'
                  }}>
                    {product.status === 'active' ? '正常' : '停用'}
                  </span>
                </Td>
                <Td>
                  <button onClick={() => handleEdit(product)} style={{ padding: '0.25rem 0.5rem', marginRight: '0.5rem', background: '#e0f2fe', color: '#0369a1', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>编辑</button>
                  <button onClick={() => handleDelete(product.id)} style={{ padding: '0.25rem 0.5rem', background: '#fee2e2', color: '#991b1b', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>删除</button>
                </Td>
              </tr>
            ))}
            {products.length === 0 && (
              <tr>
                <Td colSpan={7} style={{ textAlign: 'center', padding: '2rem', color: '#64748b' }}>
                  暂无产品数据，点击"添加产品"创建第一个产品
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

function FormField({ label, value, onChange, type = 'text', required = false }: { label: string; value: string; onChange: (v: string) => void; type?: string; required?: boolean }) {
  return (
    <div style={{ marginBottom: '1rem' }}>
      <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', fontWeight: 500, color: '#374151' }}>
        {label}
      </label>
      <input
        type={type}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        required={required}
        style={{
          width: '100%',
          padding: '0.75rem',
          border: '1px solid #d1d5db',
          borderRadius: '6px',
          fontSize: '0.875rem'
        }}
      />
    </div>
  )
}
