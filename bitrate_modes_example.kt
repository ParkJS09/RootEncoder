/**
 * 비트레이트 모드 학습 예제
 *
 * CBR vs VBR vs CQ (Constant Quality)
 */

import kotlin.random.Random

// ==========================================
// 예제 1: 기본 개념
// ==========================================

fun understandBitrateModes() {
    println("╔════════════════════════════════════════╗")
    println("║   비트레이트 모드: CBR vs VBR vs CQ   ║")
    println("╚════════════════════════════════════════╝\n")

    println("비트레이트 모드 = 인코더가 데이터 양을 조절하는 방식\n")

    println("🎯 CBR (Constant Bitrate)")
    println("   - 일정한 비트레이트 유지")
    println("   - 5 Mbps 설정 → 항상 5 Mbps")
    println("   - 스트리밍에 최적")
    println("")

    println("📊 VBR (Variable Bitrate)")
    println("   - 장면 복잡도에 따라 조절")
    println("   - 복잡한 장면 → 더 많은 비트")
    println("   - 단순한 장면 → 적은 비트")
    println("   - 평균 품질이 더 좋음")
    println("")

    println("⭐ CQ (Constant Quality)")
    println("   - 일정한 품질 유지")
    println("   - 비트레이트는 자동 조절")
    println("   - 최고 품질, 예측 불가능한 크기")
}

// ==========================================
// 예제 2: CBR 시뮬레이션
// ==========================================

fun simulateCBR(targetBitrate: Int, duration: Int): List<Pair<Int, Int>> {
    // (시간, 실제 비트레이트)
    val results = mutableListOf<Pair<Int, Int>>()

    for (second in 0 until duration) {
        // CBR은 항상 목표 비트레이트
        results.add(Pair(second, targetBitrate))
    }

    return results
}

fun demonstrateCBR() {
    println("\n=== CBR (Constant Bitrate) 시뮬레이션 ===\n")

    val targetBitrate = 5000  // 5 Mbps
    val duration = 10

    println("목표 비트레이트: ${targetBitrate} Kbps (5 Mbps)")
    println("시간: ${duration}초\n")

    val results = simulateCBR(targetBitrate, duration)

    println("시간(초)  비트레이트  장면")
    println("─────────────────────────────")

    val scenes = listOf("단순", "단순", "복잡", "복잡", "복잡", "단순", "단순", "복잡", "단순", "단순")

    for ((second, bitrate) in results) {
        val scene = scenes.getOrElse(second) { "단순" }
        val quality = when (scene) {
            "복잡" -> "★★☆☆☆ (품질 저하)"
            else -> "★★★★☆ (비트 낭비)"
        }
        println("  ${String.format("%2d", second)}      ${bitrate} Kbps    $scene $quality")
    }

    println("\n💡 CBR의 문제:")
    println("   - 복잡한 장면: 비트가 부족해서 품질 저하")
    println("   - 단순한 장면: 불필요하게 많은 비트 사용")
}

// ==========================================
// 예제 3: VBR 시뮬레이션
// ==========================================

fun simulateVBR(averageBitrate: Int, duration: Int): List<Pair<Int, Int>> {
    val results = mutableListOf<Pair<Int, Int>>()
    val scenes = listOf("simple", "simple", "complex", "complex", "complex",
                       "simple", "simple", "complex", "simple", "simple")

    for (second in 0 until duration) {
        val scene = scenes.getOrElse(second) { "simple" }

        // 장면에 따라 비트레이트 조절
        val bitrate = when (scene) {
            "complex" -> (averageBitrate * 1.5).toInt()  // 복잡 → 50% 증가
            else -> (averageBitrate * 0.7).toInt()       // 단순 → 30% 감소
        }

        results.add(Pair(second, bitrate))
    }

    return results
}

fun demonstrateVBR() {
    println("\n=== VBR (Variable Bitrate) 시뮬레이션 ===\n")

    val averageBitrate = 5000  // 평균 5 Mbps
    val duration = 10

    println("평균 비트레이트: ${averageBitrate} Kbps (5 Mbps)")
    println("시간: ${duration}초\n")

    val results = simulateVBR(averageBitrate, duration)

    println("시간(초)  비트레이트  장면        품질")
    println("──────────────────────────────────────")

    val scenes = listOf("단순", "단순", "복잡", "복잡", "복잡", "단순", "단순", "복잡", "단순", "단순")

    for ((second, bitrate) in results) {
        val scene = scenes.getOrElse(second) { "단순" }
        val quality = "★★★★★ (최적)"
        println("  ${String.format("%2d", second)}      ${bitrate} Kbps".padEnd(20) +
                "$scene".padEnd(10) + quality)
    }

    val avgActual = results.map { it.second }.average().toInt()
    val totalSize = results.sumOf { it.second } / 8  // KB

    println("\n통계:")
    println("- 평균 비트레이트: ${avgActual} Kbps")
    println("- 총 데이터: ${totalSize} KB")
    println("\n💡 VBR의 장점:")
    println("   - 복잡한 장면: 충분한 비트로 고품질")
    println("   - 단순한 장면: 비트 절약")
    println("   - 평균적으로 더 좋은 품질!")
}

// ==========================================
// 예제 4: 비교 차트
// ==========================================

fun compareModesChart() {
    println("\n=== 비트레이트 모드 비교 차트 ===\n")

    println("비트레이트 변화:")
    println("")
    println("8 Mbps │        VBR: ╱╲      ╱╲")
    println("       │           ╱  ╲    ╱  ╲")
    println("5 Mbps │ CBR: ━━━━━━━━━━━━━━━━━━━")
    println("       │        VBR:╱    ╲╱")
    println("2 Mbps │")
    println("       └────────────────────────→ 시간")
    println("         단순  복잡  단순  복잡")
    println("")

    println("품질 변화:")
    println("")
    println("높음   │ VBR: ━━━━━━━━━━━━━━━━━━━")
    println("       │")
    println("중간   │ CBR:     ╲      ╱")
    println("       │            ╲  ╱")
    println("낮음   │ CBR:         ╲╱")
    println("       └────────────────────────→ 시간")
    println("         단순  복잡  단순  복잡")
}

// ==========================================
// 예제 5: 사용 사례별 권장 모드
// ==========================================

fun recommendBitrateMode() {
    println("\n=== 사용 사례별 권장 모드 ===\n")

    val cases = listOf(
        Triple(
            "라이브 스트리밍 (YouTube, Twitch)",
            "CBR",
            "일정한 네트워크 대역폭 필요, 버퍼링 방지"
        ),
        Triple(
            "비디오 통화 (Zoom, Teams)",
            "CBR",
            "낮은 지연, 안정적인 전송"
        ),
        Triple(
            "VOD 업로드 (YouTube, Vimeo)",
            "VBR",
            "최고 품질, 파일 크기 효율"
        ),
        Triple(
            "영화/드라마 제작",
            "CQ",
            "일정한 품질, 편집 용이"
        ),
        Triple(
            "보안 카메라 녹화",
            "VBR",
            "저장 공간 절약, 품질 유지"
        ),
        Triple(
            "모바일 스트리밍",
            "CBR",
            "불안정한 네트워크 대응"
        )
    )

    for ((useCase, mode, reason) in cases) {
        println("📱 $useCase")
        println("   권장: $mode")
        println("   이유: $reason")
        println("")
    }
}

// ==========================================
// 예제 6: RootEncoder에서의 설정
// ==========================================

fun rootEncoderBitrateMode() {
    println("\n=== RootEncoder CBR 설정 ===\n")

    println("코드 (VideoEncoder.java 151-158줄):\n")
    println("```java")
    println("// CBR 모드가 지원되는지 확인")
    println("if (Build.VERSION.SDK_INT >= LOLLIPOP &&")
    println("    CodecUtil.isCBRModeSupported(encoder, type)) {")
    println("")
    println("    Log.i(TAG, \"set bitrate mode CBR\");")
    println("")
    println("    // CBR 모드 설정")
    println("    videoFormat.setInteger(")
    println("        MediaFormat.KEY_BITRATE_MODE,")
    println("        MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR")
    println("    );")
    println("} else {")
    println("    // CBR 미지원 시 기본 모드 (보통 VBR)")
    println("    Log.i(TAG, \"CBR not supported, using default\");")
    println("}")
    println("```\n")

    println("다른 모드:")
    println("- BITRATE_MODE_VBR:  Variable Bitrate")
    println("- BITRATE_MODE_CQ:   Constant Quality")
    println("- BITRATE_MODE_CBR:  Constant Bitrate (권장)")
}

// ==========================================
// 예제 7: 네트워크와 비트레이트 모드
// ==========================================

fun networkAndBitrateMode() {
    println("\n=== 네트워크 환경과 비트레이트 모드 ===\n")

    println("안정적인 네트워크 (Wi-Fi, 유선):")
    println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    println("대역폭: ━━━━━━━━━━━━━━━━━━ (일정)")
    println("CBR:    ━━━━━━━━━━━━━━━━━━ ✅ 완벽")
    println("VBR:    ━╱╲━━━╱╲━━━━━╱╲━━ ✅ 가능")
    println("결과: 둘 다 사용 가능\n")

    println("불안정한 네트워크 (모바일 데이터):")
    println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    println("대역폭: ━╱━╲━━╱╲╲━━━╱━━━━ (변동)")
    println("CBR:    ━━━━━━━━━━━━━━━━━━ ✅ 적응 가능")
    println("VBR:    ━╱╲╱╲━╱╲╱╲━╱╲━╱━━ ❌ 버퍼링 위험")
    println("결과: CBR 권장\n")

    println("💡 핵심:")
    println("   스트리밍 = CBR (예측 가능한 대역폭)")
    println("   파일 저장 = VBR (최고 품질)")
}

// ==========================================
// 예제 8: 비트레이트 모드와 품질의 관계
// ==========================================

fun qualityComparison() {
    println("\n=== 품질 비교 (같은 평균 비트레이트) ===\n")

    val avgBitrate = 5000  // 5 Mbps

    println("평균 5 Mbps로 10초 비디오 인코딩:\n")

    // CBR
    val cbrData = (0 until 10).map { avgBitrate }
    val cbrTotal = cbrData.sum() / 8  // KB
    val cbrQuality = 75  // 평균 품질 점수

    // VBR
    val vbrData = listOf(3500, 3500, 7500, 7500, 7500, 3500, 3500, 7500, 3500, 3500)
    val vbrTotal = vbrData.sum() / 8  // KB
    val vbrQuality = 85  // 평균 품질 점수

    println("┌──────────┬─────────┬──────────┬────────┐")
    println("│ 모드     │ 평균 bps│ 총 크기  │ 품질   │")
    println("├──────────┼─────────┼──────────┼────────┤")
    println("│ CBR      │ 5000 Kbps│ ${cbrTotal} KB │ ${cbrQuality}점   │")
    println("│ VBR      │ 5000 Kbps│ ${vbrTotal} KB │ ${vbrQuality}점   │")
    println("└──────────┴─────────┴──────────┴────────┘")

    println("\n💡 같은 파일 크기에서 VBR이 품질이 더 좋은 이유:")
    println("   - 비트를 효율적으로 분배")
    println("   - 필요한 곳에 더 많은 비트 할당")
}

// ==========================================
// 예제 9: 실시간 적응형 비트레이트 (ABR)
// ==========================================

fun adaptiveBitrate() {
    println("\n=== 적응형 비트레이트 (ABR) ===\n")

    println("ABR = CBR + 네트워크 모니터링")
    println("네트워크 상태에 따라 CBR 목표값을 동적으로 변경\n")

    println("시나리오:")
    println("─────────────────────────────────────────")
    println("0초:  네트워크 좋음 → CBR 5 Mbps")
    println("5초:  네트워크 느려짐 감지")
    println("6초:  CBR 2 Mbps로 변경 (품질 낮춤)")
    println("10초: 네트워크 복구 감지")
    println("11초: CBR 5 Mbps로 복귀 (품질 복원)")
    println("")

    println("구현 (RootEncoder에서):")
    println("```kotlin")
    println("// 네트워크 상태 모니터링")
    println("fun onNetworkSlow() {")
    println("    // 비트레이트 감소")
    println("    videoEncoder.setVideoBitrateOnFly(2000 * 1024)")
    println("}")
    println("")
    println("fun onNetworkRecovered() {")
    println("    // 비트레이트 복원")
    println("    videoEncoder.setVideoBitrateOnFly(5000 * 1024)")
    println("}")
    println("```")
}

// ==========================================
// 메인 함수
// ==========================================

fun main() {
    understandBitrateModes()
    demonstrateCBR()
    demonstrateVBR()
    compareModesChart()
    recommendBitrateMode()
    rootEncoderBitrateMode()
    networkAndBitrateMode()
    qualityComparison()
    adaptiveBitrate()

    println("\n" + "=".repeat(60))
    println("비트레이트 모드 학습 완료! ⚙️")
    println("=".repeat(60))
}

/**
 * 퀴즈:
 *
 * Q1. 라이브 스트리밍에는 CBR과 VBR 중 어느 것이 적합한가?
 * A: CBR. 일정한 대역폭으로 버퍼링 방지
 *
 * Q2. VBR이 CBR보다 품질이 좋은 이유는?
 * A: 비트를 효율적으로 분배. 복잡한 장면에 더 많은 비트 할당
 *
 * Q3. CBR의 단점은?
 * A: 복잡한 장면에서 비트 부족으로 품질 저하,
 *    단순한 장면에서 비트 낭비
 *
 * Q4. YouTube 업로드는 어떤 모드가 좋은가?
 * A: VBR. 최고 품질 유지, 파일 크기 효율
 *
 * Q5. RootEncoder는 기본적으로 어떤 모드를 사용?
 * A: CBR (지원되는 경우). 스트리밍에 최적화
 *
 * Q6. ABR (Adaptive Bitrate)이란?
 * A: 네트워크 상태에 따라 CBR 목표값을 동적으로 변경하는 기술
 *
 * 핵심 요약:
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * CBR: 일정 비트레이트 → 스트리밍
 * VBR: 가변 비트레이트 → 파일 저장
 * CQ:  일정 품질     → 편집/아카이브
 *
 * 스트리밍 = CBR 필수!
 * 파일 저장 = VBR 권장!
 */
