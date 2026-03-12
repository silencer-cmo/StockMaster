import { NextRequest, NextResponse } from 'next/server'
import { prisma } from '@/lib/prisma'

// GET - 获取单个仓库
export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params
    const warehouse = await prisma.warehouse.findUnique({
      where: { id }
    })
    if (!warehouse) {
      return NextResponse.json({ error: 'Warehouse not found' }, { status: 404 })
    }
    return NextResponse.json(warehouse)
  } catch (error) {
    return NextResponse.json({ error: 'Failed to fetch warehouse' }, { status: 500 })
  }
}

// PUT - 更新仓库
export async function PUT(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params
    const body = await request.json()
    const warehouse = await prisma.warehouse.update({
      where: { id },
      data: {
        code: body.code,
        name: body.name,
        address: body.address || null
      }
    })
    return NextResponse.json(warehouse)
  } catch (error: any) {
    if (error.code === 'P2002') {
      return NextResponse.json({ error: '仓库编码已存在' }, { status: 400 })
    }
    return NextResponse.json({ error: 'Failed to update warehouse' }, { status: 500 })
  }
}

// DELETE - 删除仓库
export async function DELETE(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params
    await prisma.warehouse.delete({
      where: { id }
    })
    return NextResponse.json({ success: true })
  } catch (error) {
    return NextResponse.json({ error: 'Failed to delete warehouse' }, { status: 500 })
  }
}
