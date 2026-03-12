import { NextRequest, NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'

// GET - 获取所有产品
export async function GET() {
  try {
    const products = await prisma.product.findMany({
      orderBy: { createdAt: 'desc' }
    })
    return NextResponse.json(products)
  } catch (error) {
    console.error('Failed to fetch products:', error)
    return NextResponse.json({ error: 'Failed to fetch products' }, { status: 500 })
  }
}

// POST - 创建产品
export async function POST(request: NextRequest) {
  try {
    const body = await request.json()
    const product = await prisma.product.create({
      data: {
        sku: body.sku,
        name: body.name,
        description: body.description || null,
        category: body.category || null,
        unit: body.unit || '件',
        price: body.price || 0,
        status: 'active'
      }
    })
    return NextResponse.json(product, { status: 201 })
  } catch (error: any) {
    console.error('Failed to create product:', error)
    if (error.code === 'P2002') {
      return NextResponse.json({ error: '产品编码已存在' }, { status: 400 })
    }
    return NextResponse.json({ error: 'Failed to create product' }, { status: 500 })
  }
}
