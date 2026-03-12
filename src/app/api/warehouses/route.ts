import { NextRequest, NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'

// GET - 获取所有仓库
export async function GET() {
  try {
    const warehouses = await prisma.warehouse.findMany({
      orderBy: { createdAt: 'desc' }
    })
    return NextResponse.json(warehouses)
  } catch (error) {
    console.error('Failed to fetch warehouses:', error)
    return NextResponse.json({ error: 'Failed to fetch warehouses' }, { status: 500 })
  }
}

// POST - 创建仓库
export async function POST(request: NextRequest) {
  try {
    const body = await request.json()
    const warehouse = await prisma.warehouse.create({
      data: {
        code: body.code,
        name: body.name,
        address: body.address || null,
        status: 'active'
      }
    })
    return NextResponse.json(warehouse, { status: 201 })
  } catch (error: any) {
    console.error('Failed to create warehouse:', error)
    if (error.code === 'P2002') {
      return NextResponse.json({ error: '仓库编码已存在' }, { status: 400 })
    }
    return NextResponse.json({ error: 'Failed to create warehouse' }, { status: 500 })
  }
}
