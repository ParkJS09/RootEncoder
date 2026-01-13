/**
 * 비디오 파라미터 학습 예제
 *
 * 해상도, FPS, 비트레이트의 관계를 이해합니다.
 */

import kotlin.math.*

// ==========================================
// 예제 1: 기본 개념
// ==========================================

fun understandVideoParameters() {
    println("╔════════════════════════════════════════╗")
    println("║   비디오 파라미터 학습                 ║")
    println("╚════════════════════════════════════════╝\n")

    println("3가지 핵심 파라미터:")
    println("")
    println("1️⃣  해상도 (Resolution)")
    println("   - 가로 × 세로 픽셀 수")
    println("   - 예: 1920×1080 = 2,073,600 픽셀")
    println("")
    println("2️⃣  프레임레이트 (FPS)")
    println("   - 초당 프레임 수")
    println("   - 예: 30fps = 1초에 30장의 이미지")
    println("")
    println("3️⃣  비트레이트 (Bitrate)")
    println("   - 초당 전송되는 데이터 양")
    println("   - 예: 5 Mbps = 1초에 5메가비트")
}

// ==========================================
// 예제 2: 해상도별 픽셀 수 비교
// ==========================================

data class Resolution(val name: String, val width: Int, val height: Int)

fun compareResolutions() {
    println("\n=== 일반적인 해상도 ===\n")

    val resolutions = listOf(
        Resolution("240p", 426, 240),
        Resolution("360p", 640, 360),
        Resolution("480p (SD)", 854, 480),
        Resolution("720p (HD)", 1280, 720),
        Resolution("1080p (Full HD)", 1920, 1080),
        Resolution("1440p (2K)", 2560, 1440),
        Resolution("2160p (4K)", 3840, 2160)
    )

    val base1080p = 1920 * 1080

    for (res in resolutions) {
        val pixels = res.width * res.height
        val ratio = pixels.toFloat() / base1080p
        val megaPixels = pixels / 1_000_000.0

        println("${res.name.padEnd(20)} ${res.width}×${res.height}".padEnd(35) +
                "${String.format("%.1f", megaPixels)}MP".padEnd(10) +
                "(1080p의 ${String.format("%.0f", ratio * 100)}%)")
    }
}

// ==========================================
// 예제 3: FPS 비교
// ==========================================

fun compareFPS() {
    println("\n=== 프레임레이트(FPS) 비교 ===\n")

    val fpsValues = listOf(
        Pair(24, "영화 (시네마틱)"),
        Pair(30, "TV, 유튜브 (일반)"),
        Pair(60, "게임, 스포츠 (부드러움)"),
        Pair(120, "슬로우 모션")
    )

    for ((fps, desc) in fpsValues) {
        val framesPerMinute = fps * 60
        println("${fps}fps".padEnd(8) + "- $desc (1분에 $framesPerMinute 프레임)")
    }

    println("\n💡 FPS가 2배 증가하면:")
    println("   - 데이터 양도 약 2배 증가")
    println("   - 움직임은 더 부드러워짐")
    println("   - 필요한 처리 능력도 2배")
}

// ==========================================
// 예제 4: 비트레이트 계산
// ==========================================

fun calculateBitrate(width: Int, height: Int, fps: Int, quality: String): Int {
    // 픽셀당 비트 수는 품질에 따라 다름
    val bitsPerPixel = when (quality) {
        "low" -> 0.05    // 낮은 품질
        "medium" -> 0.10 // 중간 품질
        "high" -> 0.15   // 높은 품질
        "ultra" -> 0.20  // 최고 품질
        else -> 0.10
    }

    val pixels = width * height
    val pixelsPerSecond = pixels * fps
    val bitrate = (pixelsPerSecond * bitsPerPixel).toInt()

    return bitrate
}

fun demonstrateBitrateCalculation() {
    println("\n=== 비트레이트 계산 ===\n")

    val resolutions = listOf(
        Triple(640, 480, "480p"),
        Triple(1280, 720, "720p"),
        Triple(1920, 1080, "1080p")
    )

    val qualities = listOf("low", "medium", "high", "ultra")

    println("1080p @ 30fps 기준:\n")

    for (quality in qualities) {
        val bitrate = calculateBitrate(1920, 1080, 30, quality)
        val mbps = bitrate / 1_000_000.0
        println("${quality.padEnd(8)}: ${String.format("%.1f", mbps)} Mbps")
    }

    println("\n해상도별 비교 (중간 품질, 30fps):\n")

    for ((width, height, name) in resolutions) {
        val bitrate = calculateBitrate(width, height, 30, "medium")
        val mbps = bitrate / 1_000_000.0
        println("$name".padEnd(10) + "${width}×${height}".padEnd(15) +
                "${String.format("%.1f", mbps)} Mbps")
    }
}

// ==========================================
// 예제 5: 스트리밍 데이터 양 계산
// ==========================================

fun calculateStreamingData(bitrateMbps: Double, durationMinutes: Int) {
    println("\n=== 스트리밍 데이터 계산 ===")

    val bitrateKbps = bitrateMbps * 1000
    val bitrateBps = bitrateMbps * 1_000_000
    val durationSeconds = durationMinutes * 60

    val totalBits = bitrateBps * durationSeconds
    val totalBytes = totalBits / 8
    val totalMB = totalBytes / 1_000_000
    val totalGB = totalMB / 1000

    println("비트레이트: ${bitrateMbps} Mbps")
    println("시간: $durationMinutes 분")
    println("")
    println("전송 데이터:")
    println("  - 초당: ${String.format("%.1f", bitrateMbps / 8)} MB/s")
    println("  - 분당: ${String.format("%.1f", (bitrateMbps / 8) * 60)} MB/min")
    println("  - 총: ${String.format("%.1f", totalMB)} MB (${String.format("%.2f", totalGB)} GB)")
}

// ==========================================
// 예제 6: 네트워크 대역폭 vs 비트레이트
// ==========================================

fun networkBandwidthRequirements() {
    println("\n=== 네트워크 대역폭 요구사항 ===\n")

    println("권장 대역폭 = 비트레이트 × 1.5 ~ 2.0 (버퍼 여유)\n")

    val scenarios = listOf(
        Triple("모바일 (4G)", 1.5, 10.0),
        Triple("가정용 Wi-Fi", 5.0, 50.0),
        Triple("기업용", 50.0, 1000.0)
    )

    for ((name, minMbps, maxMbps) in scenarios) {
        println("$name".padEnd(20) + "${minMbps} ~ ${maxMbps} Mbps")

        // 가능한 최대 비트레이트 (여유 2배)
        val maxBitrate = maxMbps / 2.0
        println("  → 권장 스트리밍 비트레이트: 최대 ${maxBitrate} Mbps\n")
    }

    println("💡 팁:")
    println("   - 네트워크가 불안정하면 낮은 비트레이트 사용")
    println("   - 버퍼링 = 비트레이트 > 대역폭")
}

// ==========================================
// 예제 7: 품질 vs 용량 트레이드오프
// ==========================================

fun qualityVsSize() {
    println("\n=== 품질 vs 용량 트레이드오프 ===\n")

    println("같은 해상도(1080p), 같은 FPS(30fps)에서:\n")

    val settings = listOf(
        Triple("저품질 (블록 노이즈)", 2.0, "YouTube 최소 요구사항"),
        Triple("보통 품질", 5.0, "일반 스트리밍"),
        Triple("고품질", 8.0, "YouTube 권장"),
        Triple("최고 품질", 12.0, "거의 무손실")
    )

    for ((quality, mbps, note) in settings) {
        val sizePerMin = (mbps / 8) * 60  // MB per minute
        println("${quality.padEnd(25)} ${mbps}Mbps".padEnd(40) +
                "${String.format("%.0f", sizePerMin)} MB/min")
        println("  → $note\n")
    }
}

// ==========================================
// 예제 8: RootEncoder 설정 예시
// ==========================================

fun rootEncoderSettings() {
    println("\n=== RootEncoder 실전 설정 ===\n")

    println("코드 예시:\n")
    println("```java")
    println("// VideoEncoder.java")
    println("private int width = 1920;         // Full HD")
    println("private int height = 1080;")
    println("private int fps = 30;             // 30fps")
    println("private int bitRate = 5000 * 1024;// 5 Mbps")
    println("private int iFrameInterval = 2;   // 2초마다 키프레임")
    println("```\n")

    println("설정 가이드:\n")

    println("1️⃣  모바일 데이터 스트리밍:")
    println("   - 해상도: 640×480 (480p)")
    println("   - FPS: 24")
    println("   - 비트레이트: 800 Kbps")
    println("   - 1분당: ~6 MB\n")

    println("2️⃣  Wi-Fi 일반 스트리밍:")
    println("   - 해상도: 1280×720 (720p)")
    println("   - FPS: 30")
    println("   - 비트레이트: 2500 Kbps")
    println("   - 1분당: ~19 MB\n")

    println("3️⃣  고품질 스트리밍:")
    println("   - 해상도: 1920×1080 (1080p)")
    println("   - FPS: 30")
    println("   - 비트레이트: 5000 Kbps")
    println("   - 1분당: ~38 MB\n")

    println("4️⃣  전문가용 (60fps):")
    println("   - 해상도: 1920×1080")
    println("   - FPS: 60")
    println("   - 비트레이트: 8000 Kbps")
    println("   - 1분당: ~60 MB")
}

// ==========================================
// 예제 9: 적응형 비트레이트 (ABR)
// ==========================================

fun adaptiveBitrate() {
    println("\n=== 적응형 비트레이트 (ABR) ===\n")

    println("네트워크 상태에 따라 자동으로 품질 조정:\n")

    val levels = listOf(
        Triple("최고", 1920 to 1080, 5000),
        Triple("높음", 1280 to 720, 2500),
        Triple("보통", 854 to 480, 1200),
        Triple("낮음", 640 to 360, 600),
        Triple("최저", 426 to 240, 300)
    )

    println("품질 레벨   해상도          비트레이트    네트워크")
    println("─────────────────────────────────────────────────")

    for ((level, resolution, bitrate) in levels) {
        val (width, height) = resolution
        val minBandwidth = bitrate * 1.5 / 1000.0
        println("${level.padEnd(6)}  ${width}×${height}".padEnd(20) +
                "${bitrate}Kbps".padEnd(14) + "${String.format("%.1f", minBandwidth)}Mbps+")
    }

    println("\n💡 YouTube, Netflix 등이 사용하는 기술")
    println("   네트워크가 느려지면 자동으로 낮은 품질로 전환")
}

// ==========================================
// 메인 함수
// ==========================================

fun main() {
    understandVideoParameters()
    compareResolutions()
    compareFPS()
    demonstrateBitrateCalculation()
    calculateStreamingData(5.0, 10)  // 5Mbps, 10분
    networkBandwidthRequirements()
    qualityVsSize()
    rootEncoderSettings()
    adaptiveBitrate()

    println("\n" + "=".repeat(60))
    println("비디오 파라미터 학습 완료! 🎬")
    println("=".repeat(60))
}

/**
 * 퀴즈:
 *
 * Q1. 1920×1080 @ 30fps를 5Mbps로 스트리밍하면 1분에 몇 MB?
 * A: 5 Mbps / 8 = 0.625 MB/s
 *    0.625 × 60 = 37.5 MB/min
 *
 * Q2. FPS를 30에서 60으로 올리면 데이터 양은?
 * A: 약 2배 증가 (프레임 수가 2배)
 *
 * Q3. 같은 비트레이트에서 해상도만 2배 올리면?
 * A: 화질이 나빠집니다! 같은 데이터를 4배 많은 픽셀에 분배해야 하므로.
 *
 * Q4. 모바일 4G(10Mbps)에서 안전한 스트리밍 비트레이트는?
 * A: 약 5Mbps (버퍼 여유 2배 고려)
 *
 * Q5. 해상도는 왜 항상 짝수여야 하나요?
 * A: YUV420 포맷이 2×2 픽셀 블록 단위로 색상 정보를 저장하기 때문
 *
 * 핵심 공식:
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 데이터 양 = 비트레이트 × 시간
 * 품질 = 비트레이트 / (해상도 × FPS)
 * 권장 대역폭 = 비트레이트 × 1.5 ~ 2.0
 */
