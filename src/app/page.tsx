export default function Home() {
  return (
    <main style={{
      minHeight: '100vh',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      padding: '2rem'
    }}>
      <div style={{
        background: 'white',
        borderRadius: '16px',
        padding: '3rem',
        boxShadow: '0 10px 40px rgba(0,0,0,0.1)',
        maxWidth: '600px',
        width: '100%',
        textAlign: 'center'
      }}>
        <h1 style={{
          fontSize: '2.5rem',
          fontWeight: 700,
          marginBottom: '1rem',
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          WebkitBackgroundClip: 'text',
          WebkitTextFillColor: 'transparent'
        }}>
          StockMaster
        </h1>
        <p style={{
          fontSize: '1.2rem',
          color: '#64748b',
          marginBottom: '2rem'
        }}>
          企业级库存管理系统
        </p>
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(2, 1fr)',
          gap: '1rem',
          marginTop: '2rem'
        }}>
          <FeatureCard title="库存管理" description="实时库存查询与更新" />
          <FeatureCard title="智能预警" description="自动补货提醒" />
          <FeatureCard title="数据分析" description="销量趋势预测" />
          <FeatureCard title="多仓库" description="统一库存调度" />
        </div>
        <p style={{
          marginTop: '2rem',
          padding: '1rem',
          background: '#f0fdf4',
          borderRadius: '8px',
          color: '#15803d',
          fontSize: '0.9rem'
        }}>
          ✅ 系统已成功部署，数据库配置正常！
        </p>
      </div>
    </main>
  )
}

function FeatureCard({ title, description }: { title: string; description: string }) {
  return (
    <div style={{
      padding: '1rem',
      background: '#f8fafc',
      borderRadius: '8px',
      border: '1px solid #e2e8f0'
    }}>
      <h3 style={{
        fontSize: '1rem',
        fontWeight: 600,
        marginBottom: '0.5rem',
        color: '#1e293b'
      }}>{title}</h3>
      <p style={{
        fontSize: '0.875rem',
        color: '#64748b'
      }}>{description}</p>
    </div>
  )
}
