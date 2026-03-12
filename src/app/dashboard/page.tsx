'use client'

import { useState, useEffect } from 'react'

interface Stats {
  totalProducts: number
  totalInventory: number
  totalWarehouses: number
  lowStockCount: number
}

interface DashboardProps {
  onNavigate?: (tab: 'products' | 'inventory' | 'warehouses') => void
  onQuickAction?: (action: 'addProduct' | 'stockIn' | 'stockOut' | 'inventory') => void
}

export default function Dashboard({ onNavigate, onQuickAction }: DashboardProps) {
  const [stats, setStats] = useState<Stats>({
    totalProducts: 0,
    totalInventory: 0,
    totalWarehouses: 0,
    lowStockCount: 0
  })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchStats()
  }, [])

  const fetchStats = async () => {
    try {
      const [productsRes, inventoryRes, warehousesRes] = await Promise.all([
        fetch('/api/products'),
        fetch('/api/inventory'),
        fetch('/api/warehouses')
      ])
      
      const products = await productsRes.json()
      const inventory = await inventoryRes.json()
      const warehouses = await warehousesRes.json()

      const totalInventory = inventory.reduce((sum: number, item: any) => sum + (item.quantity || 0), 0)
      const lowStockCount = inventory.filter((item: any) => item.quantity < item.minQuantity).length

      setStats({
        totalProducts: products.length,
        totalInventory,
        totalWarehouses: warehouses.length,
        lowStockCount
      })
    } catch (error) {
      console.error('Failed to fetch stats:', error)
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return <div style={{ textAlign: 'center', padding: '2rem' }}>加载中...</div>
  }

  const handleStatClick = (type: 'products' | 'inventory' | 'warehouses') => {
    if (onNavigate) {
      onNavigate(type)
    }
  }

  const handleQuickAction = (action: 'addProduct' | 'stockIn' | 'stockOut' | 'inventory') => {
    if (onQuickAction) {
      onQuickAction(action)
    }
  }

  return (
    <div>
      <h2 style={{ fontSize: '1.5rem', fontWeight: 600, marginBottom: '1.5rem', color: '#1e293b' }}>
        系统概览
      </h2>
      
      {/* 统计卡片 */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '1rem', marginBottom: '2rem' }}>
        <StatCard 
          title="产品总数" 
          value={stats.totalProducts} 
          icon="🏷️" 
          color="#3b82f6" 
          onClick={() => handleStatClick('products')}
        />
        <StatCard 
          title="库存总量" 
          value={stats.totalInventory} 
          icon="📦" 
          color="#10b981" 
          onClick={() => handleStatClick('inventory')}
        />
        <StatCard 
          title="仓库数量" 
          value={stats.totalWarehouses} 
          icon="🏭" 
          color="#8b5cf6" 
          onClick={() => handleStatClick('warehouses')}
        />
        <StatCard 
          title="库存预警" 
          value={stats.lowStockCount} 
          icon="⚠️" 
          color="#f59e0b" 
          onClick={() => handleStatClick('inventory')}
        />
      </div>

      {/* 快捷操作 */}
      <div style={{ background: 'white', borderRadius: '12px', padding: '1.5rem', boxShadow: '0 1px 3px rgba(0,0,0,0.1)' }}>
        <h3 style={{ fontSize: '1.1rem', fontWeight: 600, marginBottom: '1rem', color: '#1e293b' }}>
          快捷操作
        </h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '1rem' }}>
          <QuickAction 
            icon="➕" 
            title="添加产品" 
            description="创建新的产品记录" 
            onClick={() => handleQuickAction('addProduct')}
          />
          <QuickAction 
            icon="📥" 
            title="入库操作" 
            description="产品入库登记" 
            onClick={() => handleQuickAction('stockIn')}
          />
          <QuickAction 
            icon="📤" 
            title="出库操作" 
            description="产品出库登记" 
            onClick={() => handleQuickAction('stockOut')}
          />
          <QuickAction 
            icon="🔄" 
            title="库存盘点" 
            description="核对库存数量" 
            onClick={() => handleQuickAction('inventory')}
          />
        </div>
      </div>
    </div>
  )
}

function StatCard({ title, value, icon, color, onClick }: { title: string; value: number; icon: string; color: string; onClick?: () => void }) {
  return (
    <div 
      onClick={onClick}
      style={{
        background: 'white',
        borderRadius: '12px',
        padding: '1.5rem',
        boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
        cursor: onClick ? 'pointer' : 'default',
        transition: 'all 0.2s'
      }}
      onMouseEnter={(e) => {
        if (onClick) {
          e.currentTarget.style.transform = 'translateY(-2px)'
          e.currentTarget.style.boxShadow = '0 4px 12px rgba(0,0,0,0.15)'
        }
      }}
      onMouseLeave={(e) => {
        if (onClick) {
          e.currentTarget.style.transform = 'translateY(0)'
          e.currentTarget.style.boxShadow = '0 1px 3px rgba(0,0,0,0.1)'
        }
      }}
    >
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div>
          <p style={{ color: '#64748b', fontSize: '0.875rem', marginBottom: '0.5rem' }}>{title}</p>
          <p style={{ fontSize: '2rem', fontWeight: 700, color: '#1e293b' }}>{value}</p>
        </div>
        <div style={{
          width: '48px',
          height: '48px',
          borderRadius: '12px',
          background: `${color}20`,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          fontSize: '1.5rem'
        }}>
          {icon}
        </div>
      </div>
    </div>
  )
}

function QuickAction({ icon, title, description, onClick }: { icon: string; title: string; description: string; onClick?: () => void }) {
  return (
    <div 
      onClick={onClick}
      style={{
        padding: '1rem',
        background: '#f8fafc',
        borderRadius: '8px',
        cursor: 'pointer',
        transition: 'all 0.2s',
        border: '1px solid #e2e8f0'
      }}
      onMouseEnter={(e) => {
        e.currentTarget.style.background = '#e0f2fe'
        e.currentTarget.style.borderColor = '#3b82f6'
        e.currentTarget.style.transform = 'translateY(-2px)'
      }}
      onMouseLeave={(e) => {
        e.currentTarget.style.background = '#f8fafc'
        e.currentTarget.style.borderColor = '#e2e8f0'
        e.currentTarget.style.transform = 'translateY(0)'
      }}
    >
      <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem' }}>{icon}</div>
      <h4 style={{ fontSize: '0.9rem', fontWeight: 600, color: '#1e293b', marginBottom: '0.25rem' }}>{title}</h4>
      <p style={{ fontSize: '0.75rem', color: '#64748b' }}>{description}</p>
    </div>
  )
}
