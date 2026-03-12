import { NextRequest, NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'

// GET - 获取所有库存
export async function GET() {
  try {
    const inventory = await prisma.inventory.findMany({
      orderBy: { createdAt: 'desc' }
    })
    return NextResponse.json(inventory)
  } catch (error) {
    console.error('Failed to fetch inventory:', error)
    return NextResponse.json({ error: 'Failed to fetch inventory' }, { status: 500 })
  }
}

// POST - 创建库存记录
export async function POST(request: NextRequest) {
  try {
    const body = await request.json()
    const inventory = await prisma.inventory.create({
      data: {
        productId: body.productId,
        warehouseId: body.warehouseId,
        quantity: body.quantity || 0,
        minQuantity: body.minQuantity || 0,
        maxQuantity: body.maxQuantity || null,
        status: 'normal'
      }
    })
    return NextResponse.json(inventory, { status: 201 })
  } catch (error) {
    console.error('Failed to create inventory:', error)
    return NextResponse.json({ error: 'Failed to create inventory' }, { status: 500 })
  }
}
