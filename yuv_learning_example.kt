/**
 * YUV 색공간 학습 예제
 *
 * 이 파일은 YUV 색공간을 이해하기 위한 예제입니다.
 */

// ==========================================
// 예제 1: YUV420 메모리 레이아웃
// ==========================================

fun calculateYUV420Size(width: Int, height: Int) {
    val ySize = width * height          // Y 평면
    val uSize = (width / 2) * (height / 2)  // U 평면 (1/4)
    val vSize = (width / 2) * (height / 2)  // V 평면 (1/4)
    val totalSize = ySize + uSize + vSize   // = width × height × 1.5

    println("=== YUV420 메모리 계산 ===")
    println("해상도: ${width}x${height}")
    println("Y 평면: $ySize 바이트")
    println("U 평면: $uSize 바이트")
    println("V 평면: $vSize 바이트")
    println("총 크기: $totalSize 바이트")
    println("RGB 대비: ${(totalSize.toFloat() / (width * height * 3) * 100).toInt()}%")
}

// ==========================================
// 예제 2: YUV 픽셀 접근 패턴
// ==========================================

fun getYUV420Pixel(yuv420: ByteArray, width: Int, height: Int, x: Int, y: Int): Triple<Int, Int, Int> {
    // Y 값 (모든 픽셀에 존재)
    val yIndex = y * width + x
    val yValue = yuv420[yIndex].toInt() and 0xFF

    // U, V 값 (2x2 블록당 하나씩)
    val uvWidth = width / 2
    val uvHeight = height / 2
    val uvX = x / 2
    val uvY = y / 2

    // NV21 포맷 (UV 인터리브)
    val uvPlaneStart = width * height
    val uvIndex = uvPlaneStart + (uvY * uvWidth + uvX) * 2

    val uValue = yuv420[uvIndex].toInt() and 0xFF
    val vValue = yuv420[uvIndex + 1].toInt() and 0xFF

    return Triple(yValue, uValue, vValue)
}

// ==========================================
// 예제 3: RGB to YUV 변환 (간단 버전)
// ==========================================

fun rgbToYuv(r: Int, g: Int, b: Int): Triple<Int, Int, Int> {
    // ITU-R BT.601 표준
    val y = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
    val u = ((-0.169 * r - 0.331 * g + 0.500 * b) + 128).toInt()
    val v = ((0.500 * r - 0.419 * g - 0.081 * b) + 128).toInt()

    return Triple(
        y.coerceIn(0, 255),
        u.coerceIn(0, 255),
        v.coerceIn(0, 255)
    )
}

// ==========================================
// 예제 4: YUV to RGB 역변환
// ==========================================

fun yuvToRgb(y: Int, u: Int, v: Int): Triple<Int, Int, Int> {
    val c = y - 16
    val d = u - 128
    val e = v - 128

    val r = (1.164 * c + 1.596 * e).toInt()
    val g = (1.164 * c - 0.392 * d - 0.813 * e).toInt()
    val b = (1.164 * c + 2.017 * d).toInt()

    return Triple(
        r.coerceIn(0, 255),
        g.coerceIn(0, 255),
        b.coerceIn(0, 255)
    )
}

// ==========================================
// 예제 5: 4x4 이미지로 YUV420 이해하기
// ==========================================

fun demonstrateYUV420Layout() {
    println("\n=== 4x4 픽셀 YUV420 예제 ===")

    // 4x4 이미지 = 16개 Y 값 + 4개 U 값 + 4개 V 값 = 24바이트
    val yuv420 = ByteArray(24)

    // Y 평면 (0-15): 모든 픽셀
    println("Y 평면 (밝기):")
    for (row in 0 until 4) {
        for (col in 0 until 4) {
            val index = row * 4 + col
            print("Y${index.toString().padStart(2, '0')} ")
        }
        println()
    }

    // U 평면 (16-19): 2x2 = 4개
    println("\nU 평면 (파란색 색차):")
    for (row in 0 until 2) {
        for (col in 0 until 2) {
            val index = row * 2 + col
            print("U${index} ")
        }
        println()
    }

    // V 평면 (20-23): 2x2 = 4개
    println("\nV 평면 (빨간색 색차):")
    for (row in 0 until 2) {
        for (col in 0 until 2) {
            val index = row * 2 + col
            print("V${index} ")
        }
        println()
    }

    println("\n💡 핵심: 4개의 Y 픽셀이 하나의 U, V를 공유합니다!")
    println("   예를 들어, Y00, Y01, Y04, Y05는 모두 U0, V0를 사용")
}

// ==========================================
// 예제 6: 서브샘플링 효과 비교
// ==========================================

fun compareColorQuality() {
    println("\n=== 서브샘플링 효과 ===")

    // 빨간색 픽셀
    val red = Triple(255, 0, 0)
    val (yR, uR, vR) = rgbToYuv(red.first, red.second, red.third)
    println("빨간색 (255, 0, 0) → YUV ($yR, $uR, $vR)")

    // 초록색 픽셀
    val green = Triple(0, 255, 0)
    val (yG, uG, vG) = rgbToYuv(green.first, green.second, green.third)
    println("초록색 (0, 255, 0) → YUV ($yG, $uG, $vG)")

    // 파란색 픽셀
    val blue = Triple(0, 0, 255)
    val (yB, uB, vB) = rgbToYuv(blue.first, blue.second, blue.third)
    println("파란색 (0, 0, 255) → YUV ($yB, $uB, $vB)")

    println("\n💡 Y 값이 다르면 밝기가 다르게 보입니다!")
    println("   인간의 눈은 밝기 차이에 매우 민감합니다.")
    println("   하지만 U, V가 약간 덜 정확해도 잘 느끼지 못합니다.")
}

// ==========================================
// 메인 함수: 모든 예제 실행
// ==========================================

fun main() {
    println("╔════════════════════════════════════════╗")
    println("║   YUV 색공간 학습 예제                 ║")
    println("╚════════════════════════════════════════╝\n")

    // 예제 1: 메모리 계산
    calculateYUV420Size(1920, 1080)

    // 예제 5: 레이아웃 이해
    demonstrateYUV420Layout()

    // 예제 6: 색상 변환
    compareColorQuality()

    println("\n" + "=".repeat(50))
    println("학습 완료! 이제 YUV420 포맷을 이해하셨습니다! 🎉")
    println("=".repeat(50))
}

/**
 * 퀴즈:
 *
 * Q1. 1280x720 해상도의 YUV420 이미지는 몇 바이트일까요?
 * A: 1280 × 720 × 1.5 = 1,382,400 바이트
 *
 * Q2. 왜 YUV420을 사용하면 화질 손실이 거의 없을까요?
 * A: 인간의 눈은 색상(U, V)보다 밝기(Y)에 훨씬 민감하기 때문입니다.
 *    Y는 100% 해상도로 유지하고, U/V만 25%로 줄입니다.
 *
 * Q3. 카메라에서 받은 NV21 데이터를 왜 변환해야 할까요?
 * A: MediaCodec이 특정 YUV 포맷(I420, NV12)만 지원하기 때문입니다.
 *
 * Q4. 4개의 인접한 픽셀이 하나의 U, V를 공유한다는 것은?
 * A: 2x2 블록 안의 픽셀들이 같은 색상 정보를 사용하지만,
 *    각자 다른 밝기(Y)를 가질 수 있습니다.
 */
