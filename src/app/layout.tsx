import './globals.css'

export const metadata = {
  title: 'StockMaster - 库存管理系统',
  description: '企业级库存管理系统',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="zh-CN">
      <body>{children}</body>
    </html>
  )
}
