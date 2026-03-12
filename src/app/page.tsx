'use client'

import { useState, useEffect } from 'react'
import ProductManagement from './products/page'
import InventoryManagement from './inventory/page'
import WarehouseManagement from './warehouses/page'
import Dashboard from './dashboard/page'

type TabType = 'dashboard' | 'products' | 'inventory' | 'warehouses'

export default function Home() {
  const [activeTab, setActiveTab] = useState<TabType>('dashboard')
  const [quickAction, setQuickAction] = useState<string | null>(null)
  const [mounted, setMounted] = useState(false)

  useEffect(() => {
    setMounted(true)
  }, [])

  if (!mounted) return null

  const handleQuickAction = (action: string) => {
    setQuickAction(action)
    if (action === 'addProduct') {
      setActiveTab('products')
    } else if (action === 'stockIn' || action === 'stockOut' || action === 'inventory') {
      setActiveTab('inventory')
    }
  }

  const clearQuickAction = () => {
    setQuickAction(null)
  }

  return (
    <div style={{ minHeight: '100vh', background: '#f1f5f9' }}>
      {/* 顶部导航 */}
      <header style={{
        background: 'linear-gradient(135deg, #1e3a5f 0%, #2d5a87 100%)',
        padding: '1rem 2rem',
        boxShadow: '0 2px 10px rgba(0,0,0,0.1)'
      }}>
        <div style={{ maxWidth: '1400px', margin: '0 auto', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <h1 style={{ color: 'white', fontSize: '1.5rem', fontWeight: 700 }}>
            📦 StockMaster
          </h1>
          <nav style={{ display: 'flex', gap: '0.5rem' }}>
            <NavButton active={activeTab === 'dashboard'} onClick={() => { setActiveTab('dashboard'); clearQuickAction(); }}>
              📊 仪表盘
            </NavButton>
            <NavButton active={activeTab === 'products'} onClick={() => { setActiveTab('products'); clearQuickAction(); }}>
              🏷️ 产品管理
            </NavButton>
            <NavButton active={activeTab === 'inventory'} onClick={() => { setActiveTab('inventory'); clearQuickAction(); }}>
              📦 库存管理
            </NavButton>
            <NavButton active={activeTab === 'warehouses'} onClick={() => { setActiveTab('warehouses'); clearQuickAction(); }}>
              🏭 仓库管理
            </NavButton>
          </nav>
        </div>
      </header>

      {/* 主内容区 */}
      <main style={{ maxWidth: '1400px', margin: '0 auto', padding: '2rem' }}>
        {activeTab === 'dashboard' && (
          <Dashboard 
            onNavigate={(tab) => { setActiveTab(tab); clearQuickAction(); }}
            onQuickAction={handleQuickAction}
          />
        )}
        {activeTab === 'products' && (
          <ProductManagement 
            openAddForm={quickAction === 'addProduct'} 
            onFormOpened={clearQuickAction}
          />
        )}
        {activeTab === 'inventory' && (
          <InventoryManagement 
            openAddForm={quickAction === 'stockIn' || quickAction === 'stockOut'}
            onFormOpened={clearQuickAction}
          />
        )}
        {activeTab === 'warehouses' && <WarehouseManagement />}
      </main>
    </div>
  )
}

function NavButton({ children, active, onClick }: { children: React.ReactNode; active: boolean; onClick: () => void }) {
  return (
    <button
      onClick={onClick}
      style={{
        padding: '0.5rem 1rem',
        background: active ? 'rgba(255,255,255,0.2)' : 'transparent',
        color: 'white',
        border: 'none',
        borderRadius: '8px',
        cursor: 'pointer',
        fontSize: '0.9rem',
        fontWeight: active ? 600 : 400,
        transition: 'all 0.2s'
      }}
    >
      {children}
    </button>
  )
}
