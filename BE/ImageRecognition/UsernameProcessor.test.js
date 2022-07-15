// testing suite for UsernameProcessor

const PlayerProfile = require('../PlayerData/PlayerProfile.js')

test('Test posting username ids', () => {
    const mockTeamStats = jest.fn((ids, region) => Promise.resolve({
        summary : {
            prediction: 0.5,
            player1: new PlayerProfile('1', 'NA1'),
            player2: new PlayerProfile('2', 'NA1'),
            player3: new PlayerProfile('3', 'NA1'),
            player4: new PlayerProfile('4', 'NA1'),
            player5: new PlayerProfile('5', 'NA1'),
        }
    }))

    mockTeamStats(['1', '2', '3', '4', '5'], 'NA1')

    expect(mockTeamStats).toHaveBeenCalledTimes(1)
})